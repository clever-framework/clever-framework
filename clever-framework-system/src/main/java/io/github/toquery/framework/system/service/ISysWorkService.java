package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.constant.SysUserWorkEnum;
import io.github.toquery.framework.system.entity.SysWork;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public interface ISysWorkService extends AppBaseService<SysWork> {
    /**
     * 通过id获取所有工作信息
     *
     * @param id id
     * @return 所有工作信息
     */
    SysWork getWithFullById(Long id);

    List<SysWork> findByUserId(Long userId);

    List<SysWork> findWithFullByUserId(Long userId);

    List<SysWork> findByUserId(Long userId, SysUserWorkEnum... sysUserWorkEnums);

    List<SysWork> findByUserIds(Collection<Long> userIds);

    List<SysWork> findByUserIds(Collection<Long> userIds, SysUserWorkEnum... sysUserWorkEnums);

    /**
     * 根据用户id、部门id获取直属上级
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return 直属上级
     */
    SysWork getSupByUserIdAndDeptId(Long userId, Long deptId);

    /**
     * 根据用户id获取最低的工作级别
     *
     * @param userId 用户id
     * @return 最低的工作级别
     */
    SysWork getLowestByUserId(Long userId);

    /**
     * 根据用户id获取最高的工作级别
     *
     * @param userId 用户id
     * @return 最低的工作级别
     */
    SysWork getHighestByUserId(Long userId);

    /**
     * 根据用户id、部门id 获取最低的工作级别
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return 最低的工作级别
     */
    SysWork getLowestByUserIdAndDeptId(Long userId, Long deptId);

    /**
     * 根据用户id获取最高的工作级别
     *
     * @param userId 用户id
     * @param deptId 部门id
     * @return 最低的工作级别
     */
    SysWork getHighestByUserIdAndDeptId(Long userId, Long deptId);


}
