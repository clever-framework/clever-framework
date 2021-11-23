package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.domain.ResponseParamBuilder;
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
    public ResponseParam query() {
        return super.query();
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping(value = "/list")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseParam list() {
        return super.list();
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('system:role:add')")
    public ResponseParam saveSysRoleCheck(@Validated @RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseParam(service.saveSysRoleCheck(sysRole));
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    @PreAuthorize("hasAnyAuthority('system:role:modify')")
    public ResponseParam update(@RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseParam(service.updateSysRoleCheck(sysRole));
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('system:role:delete')")
    public void deleteSysRoleCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteSysRoleCheck(ids);
    }

    @AppLogMethod(value = SysRole.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping("{id}")
    @PreAuthorize("hasAnyAuthority('system:role:query')")
    public ResponseParam detail(@PathVariable Long id) {
        return new ResponseParamBuilder().content(super.service.getWithMenusById(id)).build();
    }
}
