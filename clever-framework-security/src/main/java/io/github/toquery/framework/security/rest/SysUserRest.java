package io.github.toquery.framework.security.rest;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.util.Date;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserRest extends AppBaseCurdController<ISysUserService, SysUser, Long> {

    @Resource
    private AppSecurityProperties appSecurityProperties;

    @Resource
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public ResponseParam query() {
        return super.query();
    }

    @GetMapping("/list")
    public ResponseParam list() {
        return super.list();
    }

    @PostMapping
    public ResponseParam saveSysUserCheck(@Validated @RequestBody SysUser sysUser) throws AppException {
        String encodePassword = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encodePassword);
        return super.handleResponseParam(service.saveSysUserCheck(sysUser));
    }

    @PutMapping
    public ResponseParam update(@RequestBody SysUser sysUser, @RequestParam(required = false, defaultValue = "000") String rootPwd) throws AppException {
        if (("admin".equalsIgnoreCase(sysUser.getUsername()) || "root".equalsIgnoreCase(sysUser.getUsername())) && !appSecurityProperties.getRootPwd().equalsIgnoreCase(rootPwd)) {
            throw new AppException("禁止修改 admin root 用户！");
        }
        return super.update(sysUser, Sets.newHashSet("nickname", "phone", "status", "email", "authorities"));
    }

    @PutMapping("/reset-password")
    public ResponseParam restPassword(@RequestBody SysUser sysUser, @RequestParam String rawPassword, @RequestParam(required = false, defaultValue = "000") String rootPwd) throws AppException {
        if (("admin".equalsIgnoreCase(sysUser.getUsername()) || "root".equalsIgnoreCase(sysUser.getUsername())) && !appSecurityProperties.getRootPwd().equalsIgnoreCase(rootPwd)) {
            throw new AppException("禁止修改 admin root 用户！");
        }
        if (Strings.isNullOrEmpty(rawPassword)){
            throw new AppException("新密码不能为空！");
        }
        sysUser.setPassword(passwordEncoder.encode(rawPassword));
        sysUser.setLastPasswordResetDate(new Date());
        return super.update(sysUser, Sets.newHashSet("password","lastPasswordResetDate"));
    }

    @DeleteMapping
    public void deleteSysUserCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteSysUserCheck(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
