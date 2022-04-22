package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.system.entity.SysConfig;
import io.github.toquery.framework.system.service.ISysConfigService;
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
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/config")
@Timed(value = "system-config", description = "系统-配置")
public class SysConfigRest extends AppBaseCrudController<ISysConfigService, SysConfig> {
    private final String[] SORT = new String[]{"sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "配置管理";


    @AppLogMethod(value = SysConfig.class, logType = AppLogType.QUERY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        return super.pageResponseResult(SORT);
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.QUERY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return super.listResponseResult(SORT);
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.CREATE, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:add')")
    @PostMapping
    public ResponseBodyWrap<?> saveSysConfigCheck(@Validated @RequestBody SysConfig sysConfig) {
        return super.handleResponseBody(domainService.saveSysConfigCheck(sysConfig));
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:modify')")
    @PutMapping
    public ResponseBodyWrap<?> updateSysConfigCheck(@RequestBody SysConfig sysConfig) {
        return super.handleResponseBody(domainService.updateSysConfigCheck(sysConfig));
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.DELETE, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteResponseResult(@RequestBody Set<Long> ids) {
        super.deleteResponseResult(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.QUERY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return super.detailResponseBody(id);
    }

    @GetMapping("value")
    public ResponseBodyWrap<?> value(@RequestParam String configName) {
        return super.handleResponseBody(domainService.value(configName));
    }
}
