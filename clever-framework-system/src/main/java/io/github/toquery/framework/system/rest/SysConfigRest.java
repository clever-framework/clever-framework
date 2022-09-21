package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.system.entity.SysConfig;
import io.github.toquery.framework.system.service.ISysConfigService;
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
@RequestMapping("/sys/config")
@Timed(value = "system-config", description = "系统-配置")
public class SysConfigRest extends AppBaseWebMvcController {
    private final String[] SORT = new String[]{"sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "配置管理";

    private final ISysConfigService sysConfigService;

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        return new ResponseBodyWrapBuilder().page(sysConfigService.page(new Page<>(this.getRequestCurrent(), this.getRequestPageSize()))).build();
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysConfigService.list());
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:add')")
    @PostMapping
    public ResponseBodyWrap<?> saveSysConfigCheck(@Validated @RequestBody SysConfig sysConfig) {
        return super.handleResponseBody(sysConfigService.saveSysConfigCheck(sysConfig));
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:modify')")
    @PutMapping
    public ResponseBodyWrap<?> updateSysConfigCheck(@RequestBody SysConfig sysConfig) {
        return super.handleResponseBody(sysConfigService.updateSysConfigCheck(sysConfig));
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:config:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteResponseResult(@RequestBody Set<Long> ids) {
        sysConfigService.removeBatchByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysConfigService.getById(id));
    }

    @GetMapping("value")
    public ResponseBodyWrap<?> value(@RequestParam String configName) {
        return super.handleResponseBody(sysConfigService.value(configName));
    }
}
