package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.system.constant.SysUserPermissionEnum;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysPermission;
import io.github.toquery.framework.system.mapper.SysUserMapper;
import io.github.toquery.framework.system.service.ISysPermissionService;
import io.github.toquery.framework.system.service.ISysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    private final PasswordEncoder passwordEncoder;

    private final ISysPermissionService sysPermissionService;


    // @Cacheable(value = "userCache", key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.loadFullUserByUsername(username);
    }


    @Override
    public UserDetails getById(Long userId) {
        return super.baseMapper.selectById(userId);
    }

    @Override
    public UserDetails loadFullUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = this.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        List<SysPermission> sysUserPermissions = sysPermissionService.findWithFullByUserId(user.getId());
        user.setUserPermissions(sysUserPermissions);

        Set<SysMenu> sysMenus = sysUserPermissions.stream().flatMap(sysUserPermission -> sysUserPermission.getRole().getMenus().stream()).collect(Collectors.toSet());
        user.setAuthorities(sysMenus);
        return user;
    }

    @Override
    public Map<Long, AppUserDetails> userDetailsMap(Set<Long> userIds) {
        List<SysUser> sysUsers = this.listByIds(userIds);
        return sysUsers.stream().collect(Collectors.toMap(SysUser::getId, Function.identity()));
    }


    @Override
    public List<SysUser> findByIds(Set<Long> ids) {
        return super.baseMapper.selectBatchIds(ids);
    }

    @Override
    public Page<SysUser> pageWithRole(int requestCurrent, int requestPageSize) {

        Page<SysUser> sysUserPage = super.page(new Page<>(requestCurrent,requestPageSize));
        List<SysUser> sysUserList = sysUserPage.getRecords();
        Set<Long> sysUserIds = sysUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
        List<SysPermission> sysUserPermissions = sysPermissionService.findByUserIds(sysUserIds, SysUserPermissionEnum.ROLE);

        Map<Long, List<SysRole>> userIdRoleMap = sysUserPermissions.stream().collect(Collectors.groupingBy(SysPermission::getUserId, Collectors.mapping(SysPermission::getRole, Collectors.toList())));
        sysUserList.forEach(sysUser -> sysUser.setRoles(userIdRoleMap.get(sysUser.getId())));

        return sysUserPage;
    }

    @Override
    public SysUser saveSysUserCheck(SysUser sysUser) throws AppException {
        String userName = sysUser.getUsername();
        if (Strings.isNullOrEmpty(userName)) {
            throw new AppException("用户名不能为空！");
        }
        if (userName.equalsIgnoreCase("admin") || userName.equalsIgnoreCase("root")) {
            throw new AppException("用户名不能为 admin root ！");
        }

        SysUser dbSysUser = this.getByUsername(userName);
        if (dbSysUser != null) {
            throw new AppException("用户已存在");
        }
        String encodePassword = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encodePassword);
        super.save(sysUser);
        return sysUser;
    }


    /**
     * 根据用户名，修改用户密码
     *
     * @param userName    用户名
     * @param rawPassword 用户新密码明文
     * @return 用户
     * @throws AppException 修改用户密码失败
     */
    @Override
    public SysUser changePassword(String userName, String sourcePassword, String rawPassword) throws AppException {
        SysUser sysUser = this.getByUsername(userName);
        if (sysUser == null) {
            throw new AppException("未找到用户");
        }
        boolean flag = passwordEncoder.matches(sourcePassword, sysUser.getPassword());
        if (!flag) {
            throw new AppException("用户原密码错误！");
        }
        String encodePassword = passwordEncoder.encode(rawPassword);
        sysUser.setPassword(encodePassword);
        sysUser.setChangePasswordDateTime(LocalDateTime.now());
        LambdaUpdateWrapper<SysUser> userWrapper = new LambdaUpdateWrapper<>();
        userWrapper.eq(SysUser::getId, sysUser.getId());
        userWrapper.set(SysUser::getPassword, sysUser.getPassword());
        userWrapper.set(SysUser::getChangePasswordDateTime, sysUser.getChangePasswordDateTime());
        super.baseMapper.update(sysUser, userWrapper);
        return sysUser;
    }

    @Override
    public void deleteSysUserCheck(Set<Long> ids) throws AppException {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUser::getId, ids);
        Optional<SysUser> sysUser = super.list(queryWrapper).stream().filter(item -> "admin".equalsIgnoreCase(item.getUsername()) || "root".equalsIgnoreCase(item.getUsername())).findAny();
        if (sysUser.isPresent()) {
            throw new AppException("删除失败，无法删除 admin root 用户！");
        }
        super.removeByIds(ids);
    }

    @Override
    public SysUser getByIdWithRole(Long id) {
        SysUser sysUser = super.getById(id);
        List<SysPermission> sysUserPermissions = sysPermissionService.findWithFullByUserId(id);
        Set<SysRole> roles = sysUserPermissions.stream().map(SysPermission::getRole).collect(Collectors.toSet());
        sysUser.setRoles(roles);
        sysUser.setRoleIds(roles.stream().map(SysRole::getId).collect(Collectors.toSet()));
        return sysUser;
    }

    @Override
    public SysUser getByUsername(String username) {
        return super.getOne(new LambdaQueryWrapper<SysUser>().eq(SysUser::getUsername, username));
    }
}
