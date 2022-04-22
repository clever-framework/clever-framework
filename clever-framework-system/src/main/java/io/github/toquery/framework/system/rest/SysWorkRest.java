package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysWork;
import io.github.toquery.framework.system.service.ISysWorkService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.micrometer.core.annotation.Timed;
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
 *
 */
@RestController
@RequestMapping("/sys/work")
@Timed(value = "system-work", description = "系统-工作")
public class SysWorkRest extends AppBaseCrudController<ISysWorkService, SysWork> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "工作管理";

    @AppLogMethod(value = SysWork.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        return super.pageResponseResult();
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return super.listResponseResult();
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:add')")
    @PostMapping
    public ResponseBodyWrap<?> save(@Validated @RequestBody SysWork sysUserWork) throws AppException {
        return super.handleResponseBody(domainService.save(sysUserWork));
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:modify')")
    @PutMapping
    public ResponseBodyWrap<?> update(@RequestBody SysWork sysWork) throws AppException {
        return super.updateResponseResult(sysWork, Sets.newHashSet("nickname", "phone", "userStatus", "email"));
    }


    @AppLogMethod(value = SysWork.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteByIds(@RequestBody Set<Long> ids) throws AppException {
        domainService.deleteByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(super.domainService.getWithFullById(id));
    }
}

