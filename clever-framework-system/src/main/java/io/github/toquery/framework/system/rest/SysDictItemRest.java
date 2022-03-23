package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.service.ISysDictItemService;
import io.github.toquery.framework.system.service.ISysDictService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
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

import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/dict/item")
@Timed(value = "system-dict", description = "系统-字典")
public class SysDictItemRest extends AppBaseCrudController<ISysDictItemService, SysDictItem> {


    private static final String[] SORT = new String[]{"sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "字典管理";

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        return super.pageResponseResult(SORT);
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:add')")
    @PostMapping
    public ResponseBodyWrap<?> saveSysDictCheck(@RequestBody SysDictItem sysDictItem) throws AppException {
        return super.handleResponseBody(domainService.saveSysDictCheck(sysDictItem));
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:modify')")
    @PutMapping
    public ResponseBodyWrap<?> updateSysDictCheck(@RequestBody SysDictItem sysDictItem) throws AppException {
        return super.handleResponseBody(domainService.updateSysDictItemCheck(sysDictItem));
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteSysDictCheck(@RequestParam Set<Long> ids) throws AppException {
        domainService.deleteByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping("/{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(super.domainService.getById(id));
    }
}
