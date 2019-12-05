package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysMenu;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysMenuService extends AppBaseService<SysMenu, Long> {
    /**
     * 自定义保存，变更上级的 hasChildren
     *
     * @param sysMenu SysMenu
     * @return SysMenu
     */
    SysMenu saveMenu(SysMenu sysMenu);

    /**
     * 通过 parentId 进行 where in查询
     *
     * @param parentId parentId
     * @return SysMenu
     */
    List<SysMenu> findByParentId(Long parentId);

    /**
     * 通过 parentIds 进行 where in查询
     *
     * @param parentIds parentId
     * @return SysMenu
     */
    List<SysMenu> findByParentIds(Set<Long> parentIds);

    /**
     * 自定义删除，变更上级的 hasChildren
     *
     * @param ids 菜单ID
     */
    void deleteMenu(Set<Long> ids);
}
