package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.service.ISysUserPermissionService;
import io.github.toquery.framework.web.domain.ResponseBody;
import io.github.toquery.framework.web.domain.ResponseBodyBuilder;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Page;
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
@RequestMapping("/sys/user/permission")
@Timed(value = "system-permission", description = "系统-权限")
public class SysUserPermissionRest extends AppBaseCrudController<ISysUserPermissionService, SysUserPermission> {


    @GetMapping
    public ResponseBody pageResponseResult() {
        Page<SysUserPermission> sysUserPermissionPage = super.domainService.queryWithRoleAndArea(super.getFilterParam(), super.getRequestCurrent(), super.getRequestPageSize());
        return new ResponseBodyBuilder().page(sysUserPermissionPage).build();
    }

    @GetMapping("/list")
    public ResponseBody listResponseResult() {
        return super.listResponseResult();
    }

    @PostMapping
    public ResponseBody saveSysUserCheck(@Validated @RequestBody SysUserPermission sysUserPermission) throws AppException {
        return super.handleResponseBody(super.domainService.saveUserPermissionCheck(sysUserPermission));
    }

    @PostMapping("/authorize")
    public ResponseBody authorize(@RequestParam Long userId, @Validated @RequestBody List<SysUserPermission> sysUserPermissions) throws AppException {
        super.domainService.authorize(userId, sysUserPermissions);
        return new ResponseBodyBuilder().success().build();
    }

    @PutMapping
    public ResponseBody updateResponseResult(@RequestBody SysUserPermission sysUserPermission) throws AppException {
        return this.handleResponseBody(super.domainService.updateUserPermissionCheck(sysUserPermission));
    }


    @DeleteMapping
    public ResponseBody deleteSysUserCheck(@RequestParam Set<Long> ids) throws AppException {
        domainService.deleteByIds(ids);
        return ResponseBody.builder().success().build();
    }

    @GetMapping("{id}")
    public ResponseBody detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(super.domainService.detailWithRoleAndArea(id));
    }
}
