package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.service.ISysMenuService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.micrometer.core.annotation.Timed;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/menu")
@Timed(value = "system-menu", description = "系统-菜单")
public class SysMenuRest extends AppBaseWebMvcController {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "菜单管理";

    private static final String[] SORT = new String[]{"parentIds_asc", "sortNum_desc"};


    private final ISysMenuService sysMenuService;

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
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysMenu> sysMenuPage = sysMenuService.page(new Page<>(super.getRequestCurrent(), super.getRequestPageSize()));
        return new ResponseBodyWrapBuilder().page(sysMenuPage).build();
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysMenuService.list());
    }

    @GetMapping("/tree")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseBodyWrap<?> tree(@RequestParam(required = false, defaultValue = "根菜单") String rootName) throws Exception {
        List<SysMenu> sysMenuList = Lists.newArrayList(new SysMenu(0L, rootName, "root"));
        List<SysMenu> childrenList = sysMenuService.list();
        sysMenuList.addAll(childrenList);
        // 将list数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseBodyWrapBuilder().content(sysMenuList).build();
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    public ResponseBodyWrap<?> saveResponseResult(@Validated @RequestBody SysMenu sysMenu) {
        return super.handleResponseBody(sysMenuService.saveMenu(sysMenu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping("/scan")
    @PreAuthorize("hasAnyAuthority('system:menu:add')")
    public ResponseBodyWrap<?> scan() {
        sysMenuService.scanAndInsertMenus(false);
        return new ResponseBodyWrapBuilder().message("添加完成").build();
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:menu:modify')")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseBodyWrap<?> updateResponseResult(@RequestBody SysMenu menu) {
        return this.handleResponseBody(sysMenuService.updateMenu(menu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('system:menu:delete')")
    public ResponseBodyWrap<?> deleteResponseResult(@RequestBody Set<Long> ids) {
        sysMenuService.deleteMenu(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('system:menu:query')")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysMenuService.getById(id));
    }
}
