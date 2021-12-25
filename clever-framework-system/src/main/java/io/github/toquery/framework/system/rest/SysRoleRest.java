package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
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

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleRest extends AppBaseCrudController<ISysRoleService, SysRole> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "角色管理";

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseResult pageResponseResult() {
        return super.pageResponseResult();
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping(value = "/list")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseResult listResponseResult() {
        return super.listResponseResult();
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    public ResponseResult saveSysRoleCheck(@Validated @RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseBody(domainService.saveSysRoleCheck(sysRole));
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:role:modify')")
    public ResponseResult updateResponseResult(@RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseBody(domainService.updateSysRoleCheck(sysRole));
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    public ResponseResult deleteSysRoleCheck(@RequestParam Set<Long> ids) throws AppException {
        domainService.deleteSysRoleCheck(ids);
        return ResponseResult.builder().success().build();
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseResult detailResponseBody(@PathVariable Long id) {
        return new ResponseBodyBuilder().content(super.domainService.getWithMenusById(id)).build();
    }
}
