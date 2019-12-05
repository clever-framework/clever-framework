package io.github.toquery.framework.log.rest;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.log.domain.SysLogVo;
import io.github.toquery.framework.system.entity.SysLog;
import io.github.toquery.framework.system.service.ISysLogService;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
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
    private ISysUserService sysUserService;

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
        List<SysLogVo> sysLogVoList = sysLogStream.map(item ->
                new SysLogVo(item, sysUserService.getById(item.getCreateUserId()))
        ).collect(Collectors.toList());
        return ResponseParam.builder().build().page(new PageImpl<>(sysLogVoList, page.getPageable(), page.getTotalElements()));
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        SysLog sysLog = super.getById(id);
        return super.handleResponseParam(new SysLogVo(sysLog, sysUserService.getById(sysLog.getCreateUserId())));
    }

    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysLog sysLog) {
        return super.save(sysLog);
    }


   /*
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




    @PutMapping
    public ResponseParam update(@RequestBody SysLog sysLog) {
        return super.update(sysLog, Sets.newHashSet("name", "code", "sortNum"));
    }

    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        super.delete(ids);
    }
    */

}
