package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.entity.SysUserPermission;
import io.github.toquery.framework.system.service.ISysUserPermissionService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.domain.ResponseParamBuilder;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/user/permission")
public class SysUserPermissiomRest extends AppBaseCrudController<ISysUserPermissionService, SysUserPermission> {


    @GetMapping
    public ResponseParam query() {
        Page<SysUserPermission> sysUserPermissionPage = super.service.queryWithRoleAndArea(super.getFilterParam(), super.getRequestCurrent(), super.getRequestPageSize());
        return new ResponseParamBuilder().page(sysUserPermissionPage).build();
    }

    @GetMapping("/list")
    public ResponseParam list() {
        return super.list();
    }

    @PostMapping
    public ResponseParam saveSysUserCheck(@Validated @RequestBody SysUserPermission sysUserPermission) throws AppException {
        return super.handleResponseParam(super.service.saveUserPermissionCheck(sysUserPermission));
    }

    @PutMapping
    public ResponseParam update(@RequestBody SysUserPermission sysUserPermission) throws AppException {
        return this.handleResponseParam(super.service.updateUserPermissionCheck(sysUserPermission));
    }


    @DeleteMapping
    public void deleteSysUserCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteByIds(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return this.handleResponseParam(super.service.detailWithRoleAndArea(id));
    }
}
