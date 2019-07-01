package io.github.toquery.framework.log.rest;


import io.github.toquery.framework.log.service.ISysLogService;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */

@RestController
@RequestMapping("/sys/log")
public class SysLogRest extends AppBaseWebMvcController {

    @Resource
    private ISysLogService sysLogService;

    @GetMapping
    public ResponseParam query() {
        return ResponseParam.builder().build().page(sysLogService.handleQuery());
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.handleResponseParam(sysLogService.getById(id));
    }
}
