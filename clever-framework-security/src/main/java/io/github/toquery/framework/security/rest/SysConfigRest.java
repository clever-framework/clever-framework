package io.github.toquery.framework.security.rest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.core.annotation.AppLogMethod;
import io.github.toquery.framework.core.constant.AppLogType;
import io.github.toquery.framework.system.entity.SysConfig;
import io.github.toquery.framework.system.service.ISysConfigService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
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
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigRest extends AppBaseCurdController<ISysConfigService, SysConfig, Long> {
    private String[] sort = new String[]{"sortNum_desc"};

    @GetMapping
    public ResponseParam query() {
        return super.query(sort);
    }

    @GetMapping("/list")
    public ResponseParam list() {
        return super.list(sort);
    }

    @GetMapping("/biz/{bizId}")
    public ResponseParam biz(@PathVariable String bizId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("bizId", bizId);
        return super.list(params, sort);
    }

    @GetMapping("/group/{configGroup}")
    public ResponseParam list(@PathVariable String configGroup) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("configGroup", configGroup);
        return super.list(params, sort);
    }


    @AppLogMethod(value = SysConfig.class, logType = AppLogType.CREA, modelName = "$moduleName", bizName = "$bizName")
    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysConfig sysConfig) {
        return super.handleResponseParam(service.save(sysConfig));
    }

    @AppLogMethod(value = SysConfig.class, logType = AppLogType.MODF, modelName = "$moduleName", bizName = "$bizName")
    @PutMapping
    public ResponseParam update(@RequestBody SysConfig sysConfig) {
        return super.update(sysConfig, Sets.newHashSet("bizId", "configGroup", "configName", "configValue"));
    }

    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        super.delete(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
