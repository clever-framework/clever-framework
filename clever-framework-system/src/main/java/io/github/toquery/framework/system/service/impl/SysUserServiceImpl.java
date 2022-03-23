package io.github.toquery.framework.system.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.constant.SysUserPermissionEnum;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.repository.SysUserRepository;
import io.github.toquery.framework.system.service.ISysUserPermissionService;
import io.github.toquery.framework.system.service.ISysUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashMap;
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
public class SysUserServiceImpl extends AppBaseServiceImpl<SysUser, SysUserRepository> implements ISysUserService {

    private final PasswordEncoder passwordEncoder;

    private final ISysUserPermissionService sysUserPermissionService;

    public SysUserServiceImpl(PasswordEncoder passwordEncoder, ISysUserPermissionService sysUserPermissionService) {
        this.passwordEncoder = passwordEncoder;
        this.sysUserPermissionService = sysUserPermissionService;
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("idIN", "id:IN");
        map.put("email", "email:EQ");
        map.put("userStatus", "userStatus:EQ");
        map.put("emailLike", "email:LIKE");
        map.put("username", "username:EQ");
        map.put("nickname", "nickname:EQ");
        map.put("usernameLike", "username:LIKE");
        return map;
    }

    @Cacheable(value = "userCache", key = "#username")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        SysUser user = super.dao.getByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
//        }
//        return user;
        return this.loadFullUserByUsername(username);
    }


    @Override
    public UserDetails loadFullUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = super.repository.getByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }
        List<SysUserPermission> sysUserPermissions = sysUserPermissionService.findWithFullByUserId(user.getId());
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
        return super.repository.findAllById(ids);
    }

    @Override
    public Page<SysUser> pageWithRole(Map<String, Object> filterParam, int requestCurrent, int requestPageSize) {

        Page<SysUser> sysUserPage = super.page(filterParam, requestCurrent, requestPageSize);
        List<SysUser> sysUserList = sysUserPage.getContent();
        Set<Long> sysUserIds = sysUserList.stream().map(SysUser::getId).collect(Collectors.toSet());
        List<SysUserPermission> sysUserPermissions = sysUserPermissionService.findByUserIds(sysUserIds, SysUserPermissionEnum.ROLE);

        Map<Long, List<SysRole>> userIdRoleMap = sysUserPermissions.stream().collect(Collectors.groupingBy(SysUserPermission::getUserId, Collectors.mapping(SysUserPermission::getRole, Collectors.toList())));
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

        SysUser dbSysUser = repository.getByUsername(userName);
        if (dbSysUser != null) {
            throw new AppException("用户已存在");
        }
//        String encodePassword = passwordEncoder.encode(sysUser.getPassword());
//        sysUser.setPassword(encodePassword);
        return save(sysUser);
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
        SysUser sysUser = repository.getByUsername(userName);
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
        return this.update(sysUser, Sets.newHashSet("password", "lastPasswordResetDate"));
    }

    @Override
    public void deleteSysUserCheck(Set<Long> ids) throws AppException {
        Map<String, Object> filter = new HashMap<>();
        filter.put("idIN", ids);
        Optional<SysUser> sysUser = super.list(filter).stream().filter(item -> "admin".equalsIgnoreCase(item.getUsername()) || "root".equalsIgnoreCase(item.getUsername())).findAny();
        if (sysUser.isPresent()) {
            throw new AppException("删除失败，无法删除 admin root 用户！");
        }
        super.deleteByIds(ids);
    }

    @Override
    public SysUser getByIdWithRole(Long id) {
        SysUser sysUser = super.getById(id);
        List<SysUserPermission> sysUserPermissions = sysUserPermissionService.findWithFullByUserId(id);
        Set<SysRole> roles = sysUserPermissions.stream().map(SysUserPermission::getRole).collect(Collectors.toSet());
        sysUser.setRoles(roles);
        sysUser.setRoleIds(roles.stream().map(SysRole::getId).collect(Collectors.toSet()));
        return sysUser;
    }
}
