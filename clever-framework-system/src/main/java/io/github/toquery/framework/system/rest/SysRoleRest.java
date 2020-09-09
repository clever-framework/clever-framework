package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.service.ISysRoleService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
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

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleRest extends AppBaseCrudController<ISysRoleService, SysRole, Long> {

    @Resource
    private AppProperties appProperties;

    @GetMapping
    public ResponseParam query() {
        return super.query();
    }

    @GetMapping(value = "/list")
    public ResponseParam list() {
        return super.list();
    }

    @PostMapping
    public ResponseParam saveSysRoleCheck(@Validated @RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseParam(service.saveSysRoleCheck(sysRole));
    }

    @PutMapping
    public ResponseParam update(@RequestBody SysRole sysRole) throws AppException {
        return super.handleResponseParam(service.updateSysRoleCheck(sysRole, Sets.newHashSet("name", "code", "menus")));
    }

    @DeleteMapping
    public void deleteSysRoleCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteSysRoleCheck(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
