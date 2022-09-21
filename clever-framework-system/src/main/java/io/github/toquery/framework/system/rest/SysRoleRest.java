package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.system.entity.SysPost;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.micrometer.core.annotation.Timed;
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

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/role")
@Timed(value = "system-role", description = "系统-角色")
public class SysRoleRest extends AppBaseWebMvcController {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "角色管理";

    private final ISysRoleService sysRoleService;

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysRole> sysRolePage = sysRoleService.page(new Page<>(super.getRequestCurrent(), super.getRequestPageSize()));
        return new ResponseBodyWrapBuilder().page(sysRolePage).build();
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping(value = "/list")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysRoleService.list());
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    public ResponseBodyWrap<?> saveSysRoleCheck(@Validated @RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseBody(sysRoleService.saveSysRoleCheck(sysRole));
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:role:modify')")
    public ResponseBodyWrap<?> updateResponseResult(@RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseBody(sysRoleService.updateSysRoleCheck(sysRole));
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    public ResponseBodyWrap<?> deleteSysRoleCheck(@RequestBody Set<Long> ids) throws AppException {
        sysRoleService.deleteSysRoleCheck(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return new ResponseBodyWrapBuilder().content(sysRoleService.getWithMenusById(id)).build();
    }
}
