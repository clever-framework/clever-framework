package io.github.toquery.framework.security.system.rest;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.security.system.domain.SysConfig;
import io.github.toquery.framework.security.system.service.ISysConfigService;
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

    @PostMapping("/group/{configGroup}")
    public ResponseParam list(@RequestParam(required = false) Long bizId,
                              @PathVariable String configGroup,
                              @RequestBody List<SysConfig> sysConfigList) {
        return super.handleResponseParam(service.reSave(bizId, configGroup, sysConfigList));
    }


    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysConfig sysConfig) {
        return super.handleResponseParam(service.save(sysConfig));
    }

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
