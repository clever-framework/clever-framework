package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.service.ISysAreaService;
import io.github.toquery.framework.system.service.ISysDictItemService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.domain.ResponseParamBuilder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/dict/item")
public class SysDictItemRest extends AppBaseCrudController<ISysDictItemService, SysDictItem, Long> {


    private String[] sort = new String[]{"sortNum_desc"};

    @GetMapping
    public ResponseParam query() {
        return super.query(sort);
    }

    @GetMapping(value = "/list")
    public ResponseParam list() {
        return super.list(sort);
    }

    @PutMapping
    public ResponseParam update(@RequestBody SysDictItem sysDictItem) throws AppException {
        return super.handleResponseParam(service.update(sysDictItem, Sets.newHashSet("itemText", "itemValue", "description", "status", "sortNum")));
    }

    @DeleteMapping
    public void deleteSysDictItemCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteByIds(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
