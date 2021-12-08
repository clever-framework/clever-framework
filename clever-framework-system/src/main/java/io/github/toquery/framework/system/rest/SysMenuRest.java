package io.github.toquery.framework.system.rest;

import com.google.common.collect.Lists;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.service.ISysMenuService;
import io.github.toquery.framework.webmvc.domain.ResponseBody;
import io.github.toquery.framework.webmvc.domain.ResponseBodyBuilder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuRest extends AppBaseCrudController<ISysMenuService, SysMenu> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "菜单管理";

    private String[] sort = new String[]{"parentIds_asc", "sortNum_desc"};

    @GetMapping
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseBody pageResponseBody() {
        return super.pageResponseBody(sort);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseBody listResponseBody() {
        return super.listResponseBody(sort);
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseBody tree(@RequestParam(required = false, defaultValue = "根菜单") String rootName) throws Exception {
        List<SysMenu> sysMenuList = Lists.newArrayList(new SysMenu(0L, rootName, "root"));
        List<SysMenu> childrenList = doaminService.find(super.getFilterParam(), sort);
        sysMenuList.addAll(childrenList);
        // 将list数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseBodyBuilder().content(sysMenuList).build();
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    public ResponseBody saveResponseBody(@Validated @RequestBody SysMenu sysMenu) {
        return super.handleResponseBody(doaminService.saveMenu(sysMenu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping("/scan")
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    public ResponseBody scan() {
        doaminService.scan();
        return new ResponseBodyBuilder().message("添加完成").build();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:menu:modify')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseBody updateResponseBody(@RequestBody SysMenu menu) {
        return this.handleResponseBody(super.doaminService.updateMenu(menu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('system:menu:delete')")
    public void delete(@RequestParam Set<Long> ids) {
        doaminService.deleteMenu(ids);
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public ResponseBody detailResponseBody(@PathVariable Long id) {
        return super.detailResponseBody(id);
    }
}
