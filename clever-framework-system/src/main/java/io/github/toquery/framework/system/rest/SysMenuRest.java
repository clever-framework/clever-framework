package io.github.toquery.framework.system.rest;

import com.google.common.collect.Lists;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.service.ISysMenuService;
import io.github.toquery.framework.webmvc.domain.ResponseBodyBuilder;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
@Timed(value = "system-menu", description = "系统-菜单")
public class SysMenuRest extends AppBaseCrudController<ISysMenuService, SysMenu> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "菜单管理";

    private static final String[] SORT = new String[]{"parentIds_asc", "sortNum_desc"};

    @ApiResponse(description = "分页查询系统菜单",
            content = {
                    @Content(mediaType = "application/json",
                            array =
                            @ArraySchema(schema = @Schema(implementation = SysMenu.class))
                    )
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseResult pageResponseResult() {
        return super.pageResponseResult(SORT);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseResult listResponseResult() {
        return super.listResponseResult(SORT);
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseResult tree(@RequestParam(required = false, defaultValue = "根菜单") String rootName) throws Exception {
        List<SysMenu> sysMenuList = Lists.newArrayList(new SysMenu(0L, rootName, "root"));
        List<SysMenu> childrenList = domainService.list(super.getFilterParam(), SORT);
        sysMenuList.addAll(childrenList);
        // 将list数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseBodyBuilder().content(sysMenuList).build();
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    public ResponseResult saveResponseResult(@Validated @RequestBody SysMenu sysMenu) {
        return super.handleResponseBody(domainService.saveMenu(sysMenu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping("/scan")
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    public ResponseResult scan() {
        domainService.scanAndInsertMenus(false);
        return new ResponseBodyBuilder().message("添加完成").build();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:menu:modify')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseResult updateResponseResult(@RequestBody SysMenu menu) {
        return this.handleResponseBody(super.domainService.updateMenu(menu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('system:menu:delete')")
    public ResponseResult deleteResponseResult(@RequestParam Set<Long> ids) {
        domainService.deleteMenu(ids);
        return ResponseResult.builder().success().build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public ResponseResult detailResponseBody(@PathVariable Long id) {
        return super.detailResponseBody(id);
    }
}
