package io.github.toquery.framework.log.rest.controller;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.log.annotation.AppLogMethod;
import io.github.toquery.framework.log.rest.entity.SysLog;
import io.github.toquery.framework.log.rest.service.ISysLogService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.format.annotation.DateTimeFormat;
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

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */

@RestController
@RequestMapping("/sys/log")
public class SysLogRest extends AppBaseCurdController<ISysLogService, SysLog, Long> {


    @GetMapping
    public ResponseParam query(@RequestParam(value = "filter_createDateGT", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") Date createDataGT,
                               @RequestParam(value = "filter_createDateLT", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") Date createDataLT) {
        Map<String, Object> filterParam = super.getFilterParam();
        if (createDataGT != null && createDataLT != null) {
            filterParam.put("createDateGT", createDataGT);
            filterParam.put("createDateLT", createDataLT);
        }
        return super.query(filterParam);
    }

    @GetMapping("/list")
    public ResponseParam list(@RequestParam(value = "filter_createDateGT", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") Date createDataGT,
                              @RequestParam(value = "filter_createDateLT", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME) @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") Date createDataLT) {
        Map<String, Object> filterParam = super.getFilterParam();
        if (createDataGT != null && createDataLT != null) {
            filterParam.put("createDateGT", createDataGT);
            filterParam.put("createDateLT", createDataLT);
        }
        return super.list(filterParam);
    }



    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysLog sysLog) {
        return super.save(sysLog);
    }

    @PutMapping
    public ResponseParam update(@RequestBody SysLog sysLog) {
        return super.update(sysLog, Sets.newHashSet("name", "code", "sortNum"));
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