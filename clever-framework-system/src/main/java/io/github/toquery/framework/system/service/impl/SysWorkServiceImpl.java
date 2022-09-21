package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.toquery.framework.system.constant.SysUserWorkEnum;
import io.github.toquery.framework.system.entity.SysDept;
import io.github.toquery.framework.system.entity.SysPost;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysWork;
import io.github.toquery.framework.system.mapper.SysWorkMapper;
import io.github.toquery.framework.system.service.ISysDeptService;
import io.github.toquery.framework.system.service.ISysPostService;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.system.service.ISysWorkService;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysWorkServiceImpl extends ServiceImpl<SysWorkMapper, SysWork> implements ISysWorkService {

    private final ISysUserService sysUserService;

    private final ISysDeptService sysDeptService;

    private final ISysPostService sysPostService;

    public SysWorkServiceImpl(ISysUserService sysUserService, ISysDeptService sysDeptService, ISysPostService sysPostService) {
        this.sysUserService = sysUserService;
        this.sysDeptService = sysDeptService;
        this.sysPostService = sysPostService;
    }


    /**
     * 查询条件表达式
     */
    public static final Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("ids", "id:IN");
            put("userId", "userId:EQ");
            put("deptId", "deptId:EQ");
            put("postId", "postId:EQ");
            put("userIds", "userId:IN");
            put("deptIds", "deptId:IN");
            put("postIds", "postId:IN");
        }
    };

//    @Override
//    public Map<String, String> getQueryExpressions() {
//        return expressionMap;
//    }

    @Override
    public SysWork getWithFullById(Long id) {
        SysWork sysWork = super.getById(id);
        sysWork.setUser((SysUser)sysUserService.getById(sysWork.getUserId()));
        sysWork.setDept(sysDeptService.getById(sysWork.getDeptId()));
        sysWork.setPost(sysPostService.getById(sysWork.getPostId()));
        return sysWork;
    }

    @Override
    public List<SysWork> findByUserId(Long userId) {
        return null;
    }

    @Override
    public List<SysWork> findByUserId(Long userId, SysUserWorkEnum... sysUserWorkEnums) {
        return this.findByUserIds(Sets.newHashSet(userId), sysUserWorkEnums);
    }

    @Override
    public List<SysWork> findByUserIds(Collection<Long> userIds) {
        LambdaQueryWrapper<SysWork> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysWork::getUserId, userIds);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public List<SysWork> findByUserIds(Collection<Long> userIds, SysUserWorkEnum... sysUserWorkEnums) {
        List<SysWork> sysWorks = this.findByUserIds(userIds);

        if (sysUserWorkEnums == null || sysUserWorkEnums.length < 1) {
            return sysWorks;
        }
        List<SysUserWorkEnum> sysUserPermissionEnumStream = Arrays.asList(sysUserWorkEnums);

        Map<Long, SysUser> sysUserMap = new HashMap<>();
        Map<Long, SysDept> sysDeptMap = new HashMap<>();
        Map<Long, SysPost> sysPostMap = new HashMap<>();

        if (sysUserPermissionEnumStream.contains(SysUserWorkEnum.USER)) {
            Set<Long> sysUserIds = sysWorks.stream().map(SysWork::getUserId).collect(Collectors.toSet());
            sysUserMap = sysUserService.listByIds(sysUserIds).stream().collect(Collectors.toMap(SysUser::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserWorkEnum.DEPT)) {
            Set<Long> sysDeptIds = sysWorks.stream().map(SysWork::getDeptId).collect(Collectors.toSet());
            sysDeptMap = sysDeptService.listByIds(sysDeptIds).stream().collect(Collectors.toMap(SysDept::getId, item -> item, (n, o) -> n));
        }

        if (sysUserPermissionEnumStream.contains(SysUserWorkEnum.POST)) {
            Set<Long> sysPostIds = sysWorks.stream().map(SysWork::getPostId).collect(Collectors.toSet());
            sysPostMap = sysPostService.listByIds(sysPostIds).stream().collect(Collectors.toMap(SysPost::getId, item -> item, (n, o) -> n));
        }


        for (SysWork sysUserPermission : sysWorks) {
            sysUserPermission.setUser(sysUserMap.get(sysUserPermission.getUserId()));
            sysUserPermission.setDept(sysDeptMap.get(sysUserPermission.getDeptId()));
            sysUserPermission.setPost(sysPostMap.get(sysUserPermission.getPostId()));
        }

        return sysWorks;
    }

    @Override
    public List<SysWork> findWithFullByUserId(Long userId) {
        return null;
    }

    /**
     * 根据用户id、部门id获取直属上级
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return 直属上级
     */
    @Override
    public SysWork getSupByUserIdAndDeptId(Long userId, Long deptId) {
        return null;
    }

    /**
     * 根据用户id获取最低的工作级别
     *
     * @param userId 用户id
     * @return 最低的工作级别
     */
    @Override
    public SysWork getLowestByUserId(Long userId) {
        return null;
    }

    /**
     * 根据用户id获取最高的工作级别
     *
     * @param userId 用户id
     * @return 最低的工作级别
     */
    @Override
    public SysWork getHighestByUserId(Long userId) {
        return null;
    }

    /**
     * 根据用户id、部门id 获取最低的工作级别
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return 最低的工作级别
     */
    @Override
    public SysWork getLowestByUserIdAndDeptId(Long userId, Long deptId) {
        return null;
    }

    /**
     * 根据用户id获取最高的工作级别
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return 最低的工作级别
     */
    @Override
    public SysWork getHighestByUserIdAndDeptId(Long userId, Long deptId) {
        return null;
    }
}
