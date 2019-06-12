package io.github.toquery.framework.security.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.security.domain.SysUser;
import io.github.toquery.framework.security.service.ISysUserService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserRest extends AppBaseCurdController<ISysUserService, SysUser, Long> {

    @GetMapping
    public ResponseParam query() {
        return ResponseParam.builder().build().page(super.handleQuery());
    }

    @GetMapping("/list")
    public ResponseParam list() {
        return ResponseParam.builder().build().content(super.handleList());
    }

    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysUser sysUser) {
        return ResponseParam.builder().build().content(service.save(sysUser));
    }

    @PutMapping
    public ResponseParam update(@RequestBody SysUser sysUser) {
        return ResponseParam.builder().build().content(service.update(sysUser, Sets.newHashSet("loginName","roles")));
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable Long id) {
        service.deleteById(id);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return ResponseParam.builder().build().content(service.getById(id));
    }
}
