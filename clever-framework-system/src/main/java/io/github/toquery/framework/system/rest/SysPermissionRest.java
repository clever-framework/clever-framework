package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.system.entity.SysPermission;
import io.github.toquery.framework.system.service.ISysPermissionService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/sys/permission")
@Timed(value = "system-permission", description = "系统-权限")
public class SysPermissionRest extends AppBaseWebMvcController {

    private final ISysPermissionService sysPermissionService;

    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysPermission> sysUserPermissionPage = sysPermissionService.queryWithRoleAndArea(super.getRequestCurrent(), super.getRequestPageSize());
        return new ResponseBodyWrapBuilder().page(sysUserPermissionPage).build();
    }

    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysPermissionService.list());
    }

    @PostMapping
    public ResponseBodyWrap<?> saveSysUserCheck(@Validated @RequestBody SysPermission sysUserPermission) throws AppException {
        return super.handleResponseBody(sysPermissionService.saveUserPermissionCheck(sysUserPermission));
    }

    @PostMapping("/authorize")
    public ResponseBodyWrap<?> authorize(@RequestParam Long userId, @Validated @RequestBody List<SysPermission> sysUserPermissions) throws AppException {
        sysPermissionService.authorize(userId, sysUserPermissions);
        return new ResponseBodyWrapBuilder().success().build();
    }

    @PutMapping
    public ResponseBodyWrap<?> updateResponseResult(@RequestBody SysPermission sysUserPermission) throws AppException {
        return this.handleResponseBody(sysPermissionService.updateUserPermissionCheck(sysUserPermission));
    }


    @DeleteMapping
    public ResponseBodyWrap<?> deleteSysUserCheck(@RequestBody Set<Long> ids) throws AppException {
        sysPermissionService.removeBatchByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysPermissionService.detailWithRoleAndArea(id));
    }
}
