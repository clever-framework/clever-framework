package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.service.ISysDictService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
public class SysDictRest extends AppBaseCrudController<ISysDictService, SysDict> {


    private String[] sort = new String[]{"sortNum_desc"};

    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping
    public ResponseParam query() {
        return super.query(sort);
    }

    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/list")
    public ResponseParam list() {
        return super.list(sort);
    }

    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/code/{dictCode}")
    public ResponseParam code(@PathVariable String dictCode) {
        return this.handleResponseParam(super.service.findDictItemByDictCode(dictCode));
    }

    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping(value = "/item/{dictCode}/{itemValue}")
    public ResponseParam item(@PathVariable String dictCode, @PathVariable String itemValue) {
        return this.handleResponseParam(super.service.item(dictCode, itemValue));
    }

    @PreAuthorize("hasAnyAuthority('system:dict:modify')")
    @PutMapping
    public ResponseParam updateSysDictCheck(@RequestBody SysDict sysDict) throws AppException {
        return super.handleResponseParam(service.updateSysDictCheck(sysDict));
    }

    @PreAuthorize("hasAnyAuthority('system:dict:delete')")
    @DeleteMapping
    public void deleteSysDictCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteByIds(ids);
    }

    @PreAuthorize("hasAnyAuthority('system:dict:query')")
    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return this.handleResponseParam(super.service.getWithItemById(id));
    }
}
