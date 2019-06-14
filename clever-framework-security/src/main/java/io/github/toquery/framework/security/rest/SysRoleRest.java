package io.github.toquery.framework.security.rest;

import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.security.domain.SysRole;
import io.github.toquery.framework.security.service.ISysRoleService;
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

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleRest extends AppBaseCurdController<ISysRoleService, SysRole, Long> {

    @GetMapping
    public ResponseParam query() {
        return super.query();
    }

    @GetMapping(value = "/list")
    public ResponseParam list() {
        return super.list();
    }

    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysRole sysUser) {
        return super.save(sysUser);
    }

    @PutMapping
    public ResponseParam update(SysRole sysUser) {
        return super.update(sysUser, null);
    }

    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        super.delete(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
