package io.github.toquery.framework.system.rest;

import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
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
import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserRest extends AppBaseCrudController<ISysUserService, SysUser> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "用户管理";

    @Resource
    private AppProperties appProperties;

    @Resource
    private PasswordEncoder passwordEncoder;

    @AppLogMethod(value = SysUser.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping
    public ResponseParam query() {
        return super.query();
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping("/list")
    public ResponseParam list() {
        return super.list();
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseParam saveSysUserCheck(@Validated @RequestBody SysUser sysUser) throws AppException {
        String encodePassword = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encodePassword);
        return super.handleResponseParam(service.saveSysUserCheck(sysUser));
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseParam update(@RequestBody SysUser sysUser, @RequestParam(required = false, defaultValue = "000") String rootPwd) throws AppException {
        if (("admin".equalsIgnoreCase(sysUser.getUsername()) || "root".equalsIgnoreCase(sysUser.getUsername())) && !appProperties.getRootPassword().equalsIgnoreCase(rootPwd)) {
            throw new AppException("禁止修改 admin root 用户！");
        }
        return super.update(sysUser, Sets.newHashSet("nickname", "phone", "userStatus", "email"));
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping("/reset-password")
    public ResponseParam restPassword(@RequestBody SysUser sysUser, @RequestParam String rawPassword, @RequestParam(required = false, defaultValue = "000") String rootPwd) throws AppException {
        if (("admin".equalsIgnoreCase(sysUser.getUsername()) || "root".equalsIgnoreCase(sysUser.getUsername())) && !appProperties.getRootPassword().equalsIgnoreCase(rootPwd)) {
            throw new AppException("禁止修改 admin root 用户！");
        }
        if (Strings.isNullOrEmpty(rawPassword)) {
            throw new AppException("新密码不能为空！");
        }
        sysUser.setPassword(passwordEncoder.encode(rawPassword));
        sysUser.setChangePasswordDateTime(LocalDateTime.now());
        return super.update(sysUser, Sets.newHashSet("password", "lastPasswordResetDate"));
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void deleteSysUserCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteSysUserCheck(ids);
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
