package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.service.ISysDictService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * @author toquery
 * @version 1
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/dict")
@Timed(value = "system-dict", description = "系统-字典")
public class SysDictRest extends AppBaseWebMvcController {


    private static final String[] SORT = new String[]{"sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "字典管理";

    private final ISysDictService sysDictService;

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysDict> sysDictPage = sysDictService.page(new Page<>(super.getRequestCurrent(), super.getRequestPageSize()));
        return new ResponseBodyWrapBuilder().page(sysDictPage).build();
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysDictService.list());
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/codes/{dictCode}")
    public ResponseBodyWrap<?> code(@PathVariable String dictCode) {
        return this.handleResponseBody(sysDictService.findDictItemByDictCode(dictCode));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/codes/{dictCode}/{itemValue}")
    public ResponseBodyWrap<?> item(@PathVariable String dictCode, @PathVariable String itemValue) {
        return this.handleResponseBody(sysDictService.item(dictCode, itemValue));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:add')")
    @PostMapping
    public ResponseBodyWrap<?> saveSysDictCheck(@RequestBody SysDict sysDict) throws AppException {
        return super.handleResponseBody(sysDictService.saveSysDictCheck(sysDict));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:modify')")
    @PutMapping
    public ResponseBodyWrap<?> updateSysDictCheck(@RequestBody SysDict sysDict) throws AppException {
        return super.handleResponseBody(sysDictService.updateSysDictCheck(sysDict));
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteSysDictCheck(@RequestBody Set<Long> ids) throws AppException {
        sysDictService.deleteSysDictCheck(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysDict.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysDictService.getWithItemById(id));
    }
}
