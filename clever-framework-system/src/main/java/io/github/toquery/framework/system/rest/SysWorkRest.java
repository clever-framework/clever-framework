package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.system.entity.SysWork;
import io.github.toquery.framework.system.service.ISysWorkService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 *
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/work")
@Timed(value = "system-work", description = "系统-工作")
public class SysWorkRest extends AppBaseWebMvcController {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "工作管理";


    private final ISysWorkService sysWorkService;

    @AppLogMethod(value = SysWork.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        return new ResponseBodyWrapBuilder().page(sysWorkService.page(new Page<>(this.getRequestCurrent(), this.getRequestPageSize()))).build();
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysWorkService.list());
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:add')")
    @PostMapping
    public ResponseBodyWrap<?> save(@Validated @RequestBody SysWork sysUserWork) throws AppException {
        return super.handleResponseBody(sysWorkService.save(sysUserWork));
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:modify')")
    @PutMapping
    public ResponseBodyWrap<?> update(@RequestBody SysWork sysWork) throws AppException {
        return super.handleResponseBody(sysWorkService.updateById(sysWork));
    }


    @AppLogMethod(value = SysWork.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteByIds(@RequestBody Set<Long> ids) throws AppException {
        sysWorkService.removeBatchByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysWork.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:work:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysWorkService.getById(id));
    }
}

