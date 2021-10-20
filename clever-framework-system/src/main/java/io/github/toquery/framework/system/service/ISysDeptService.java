package io.github.toquery.framework.system.service;

import com.google.common.collect.Sets;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysDept;
import io.github.toquery.framework.system.entity.SysDept;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysDeptService extends AppBaseService<SysDept> {

    public static final String ROOT_ID = "0";

    public static final Set<String> UPDATE_FIELD = Sets.newHashSet("menuName", "menuCode", "sortNum", "parentId", "parentIds", "treePath", "hasChildren");

    /**
     * 自定义保存，变更上级的 hasChildren
     *
     * @param sysMenu SysDept
     * @return SysDept
     */
    SysDept saveDept(SysDept sysMenu);

    /**
     * 自定义修改，变更上级的 hasChildren
     *
     * @param sysDept SysDept
     * @return SysDept
     */
    SysDept updateDept(SysDept sysDept);

    /**
     * 通过 parentId 进行 where in查询
     *
     * @param parentId parentId
     * @return SysDept
     */
    List<SysDept> findByParentId(Long parentId);

    /**
     * 通过 parentIds 进行 where in查询
     *
     * @param parentIds parentId
     * @return SysDept
     */
    List<SysDept> findByParentIds(Set<Long> parentIds);


    List<SysDept> findAllChildren(Long parentId);

    /**
     * 自定义删除，变更上级的 hasChildren
     *
     * @param ids 菜单ID
     */
    void deleteDept(Set<Long> ids);

}
