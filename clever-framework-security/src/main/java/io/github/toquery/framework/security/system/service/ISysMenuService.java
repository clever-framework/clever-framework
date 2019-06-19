package io.github.toquery.framework.security.system.service;

import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.security.system.domain.SysMenu;

import java.util.List;
import java.util.Set;
// import io.github.toquery.cleverweb.MenuDao;

/**
 * @author toquery
 * @version 1
 */
public interface ISysMenuService extends AppBaseService<SysMenu, Long> {
    List<SysMenu> findTree();

    List<SysMenu> permission();

    SysMenu saveMenu(SysMenu sysMenu);

    void deleteMenu(Set<Long> ids);
}
