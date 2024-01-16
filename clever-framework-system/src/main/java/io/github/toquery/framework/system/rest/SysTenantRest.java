package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.system.entity.SysTenant;
import io.github.toquery.framework.system.service.ISysTenantService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/tenant")
@Timed(value = "system-user", description = "系统-租户")
public class SysTenantRest extends AppBaseWebMvcController {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "租户管理";

    private final ISysTenantService sysTenantService;

    @AppLogMethod(value = SysTenant.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:tenant:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysTenant> page = sysTenantService.page(new Page<>(this.getRequestCurrent(), this.getRequestPageSize()));
        return new ResponseBodyWrapBuilder().page(page).build();
    }

    @AppLogMethod(value = SysTenant.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:tenant:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysTenantService.list());
    }


    @AppLogMethod(value = SysTenant.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:tenant:add')")
    @PostMapping
    public ResponseBodyWrap<?> saveSysTenantCheck(@Validated @RequestBody SysTenant sysTenant) throws AppException {
        return super.handleResponseBody(sysTenantService.saveSysTenantCheck(sysTenant));
    }

    @AppLogMethod(value = SysTenant.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:tenant:modify')")
    @PutMapping
    public ResponseBodyWrap<?> update(@RequestBody SysTenant sysTenant) throws AppException {
        return super.handleResponseBody(sysTenantService.updateSysTenantCheck(sysTenant));
    }


    @AppLogMethod(value = SysTenant.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:tenant:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteSysTenantCheck(@RequestParam Set<Long> ids) throws AppException {
        sysTenantService.deleteSysTenantCheck(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysTenant.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:tenant:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysTenantService.getById(id));
    }
}
