package io.github.toquery.framework.log.rest;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.log.entity.SysLog;
import io.github.toquery.framework.log.service.ISysLogService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.domain.ResponseParamBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogRest extends AppBaseCrudController<ISysLogService, SysLog, Long> {

    @Resource
    private AppUserDetailService userDetailsService;

    private static final String[] PAGE_SORT = new String[]{"createDatetime_desc"};

    @GetMapping
    public ResponseParam query(@RequestParam(value = "filter_createDatetimeGT", required = false) @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME) @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN) Date createDataGT,
                               @RequestParam(value = "filter_createDatetimeLT", required = false) @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME) @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN) Date createDataLT) {
        Map<String, Object> filterParam = super.getFilterParam();
        if (createDataGT != null && createDataLT != null) {
            filterParam.put("createDatetimeGT", createDataGT);
            filterParam.put("createDatetimeLT", createDataLT);
        }
        Page<SysLog> page = super.handleQuery(filterParam, PAGE_SORT);
        List<SysLog> sysLogList = page.getContent();
        Stream<SysLog> sysLogStream = sysLogList.size() > 6 ? sysLogList.parallelStream() : sysLogList.stream();
        List<SysLog> sysLogVoList = sysLogStream.map(item -> {
            Long userId = item.getUserId();
            userId = userId == null ? item.getCreateUserId() : userId;
            return new SysLog(item, userDetailsService.getById(userId));
        }).collect(Collectors.toList());
        return new ResponseParamBuilder().page(new PageImpl<>(sysLogVoList, page.getPageable(), page.getTotalElements())).build();
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        SysLog sysLog = super.getById(id);
        Long userId = sysLog.getUserId();
        userId = userId == null ? sysLog.getCreateUserId() : userId;
        return super.handleResponseParam(new SysLog(sysLog, userDetailsService.getById(userId)));
    }

    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysLog sysLog) {
        return super.save(sysLog);
    }


}
