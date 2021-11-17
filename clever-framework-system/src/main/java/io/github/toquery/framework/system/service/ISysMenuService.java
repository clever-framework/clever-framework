package io.github.toquery.framework.system.service;

import com.google.common.collect.Sets;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.webmvc.domain.ResponsePage;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysMenuService extends AppBaseService<SysMenu> {

    String ROOT_ID = "0";

    Set<String> UPDATE_FIELD = Sets.newHashSet("menuName", "menuCode", "sortNum", "parentId", "parentIds", "treePath", "hasChildren");

    /**
     * 自定义保存，变更上级的 hasChildren
     *
     * @param sysMenu SysMenu
     * @return SysMenu
     */
    SysMenu saveMenu(SysMenu sysMenu);

    /**
     * 扫码包含使用注解的类，并自动添加到数据库中
     *
     */
    void scan();

    /**
     * 自定义修改，变更上级的 hasChildren
     *
     * @param sysMenu SysMenu
     * @return SysMenu
     */
    SysMenu updateMenu(SysMenu sysMenu);

    public List<SysMenu> findByMenuCode(String menuCode);

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


    List<SysMenu> findAllChildren(Long parentId);

    /**
     * 自定义删除，变更上级的 hasChildren
     *
     * @param ids 菜单ID
     */
    void deleteMenu(Set<Long> ids);

}
