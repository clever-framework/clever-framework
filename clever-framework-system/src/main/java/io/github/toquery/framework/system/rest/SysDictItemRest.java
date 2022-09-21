package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.service.ISysDictItemService;
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
@RequestMapping("/sys/dict/item")
@Timed(value = "system-dict", description = "系统-字典")
public class SysDictItemRest extends AppBaseWebMvcController {


    private static final String[] SORT = new String[]{"sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "字典管理";

    private final ISysDictItemService sysDictItemService;

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysDictItem> sysDictItemPage = sysDictItemService.page(new Page<>(super.getRequestCurrent(), super.getRequestPageSize()));
        return new ResponseBodyWrapBuilder().page(sysDictItemPage).build();
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:add')")
    @PostMapping
    public ResponseBodyWrap<?> saveSysDictCheck(@RequestBody SysDictItem sysDictItem) throws AppException {
        return super.handleResponseBody(sysDictItemService.saveSysDictCheck(sysDictItem));
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:modify')")
    @PutMapping
    public ResponseBodyWrap<?> updateSysDictCheck(@RequestBody SysDictItem sysDictItem) throws AppException {
        return super.handleResponseBody(sysDictItemService.updateSysDictItemCheck(sysDictItem));
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteSysDictCheck(@RequestBody Set<Long> ids) throws AppException {
        sysDictItemService.removeBatchByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysDictItem.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping("/{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysDictItemService.getById(id));
    }
}
