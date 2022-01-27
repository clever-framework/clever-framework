package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.service.ISysDictService;
import io.github.toquery.framework.web.domain.ResponseBody;
import io.micrometer.core.annotation.Timed;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/sys/dict")
@Timed(value = "system-dict", description = "系统-字典")
public class SysDictRest extends AppBaseCrudController<ISysDictService, SysDict> {


    private static final String[] SORT = new String[]{"sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "字典管理";

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping
    public ResponseBody pageResponseResult() {
        return super.pageResponseResult(SORT);
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/list")
    public ResponseBody listResponseResult() {
        return super.listResponseResult(SORT);
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/codes/{dictCode}")
    public ResponseBody code(@PathVariable String dictCode) {
        return this.handleResponseBody(super.domainService.findDictItemByDictCode(dictCode));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/codes/{dictCode}/{itemValue}")
    public ResponseBody item(@PathVariable String dictCode, @PathVariable String itemValue) {
        return this.handleResponseBody(super.domainService.item(dictCode, itemValue));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:add')")
    @PostMapping
    public ResponseBody saveSysDictCheck(@RequestBody SysDict sysDict) throws AppException {
        return super.handleResponseBody(domainService.saveSysDictCheck(sysDict));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:modify')")
    @PutMapping
    public ResponseBody updateSysDictCheck(@RequestBody SysDict sysDict) throws AppException {
        return super.handleResponseBody(domainService.updateSysDictCheck(sysDict));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:delete')")
    @DeleteMapping
    public ResponseBody deleteSysDictCheck(@RequestParam Set<Long> ids) throws AppException {
        domainService.deleteSysDictCheck(ids);
        return ResponseBody.builder().success().build();
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping("{id}")
    public ResponseBody detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(super.domainService.getWithItemById(id));
    }
}
