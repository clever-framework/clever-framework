package io.github.toquery.framework.system.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.service.ISysUserService;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.micrometer.core.annotation.Timed;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
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
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/user")
@Timed(value = "system-user", description = "系统-用户")
public class SysUserRest extends AppBaseCrudController<ISysUserService, SysUser> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "用户管理";

    @Resource
    private AppProperties appProperties;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private ObjectMapper objectMapper;

    @AppLogMethod(value = SysUser.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysUser> page = domainService.pageWithRole(super.getFilterParam(), super.getRequestCurrent(), super.getRequestPageSize());
        return new ResponseBodyWrapBuilder().page(page).build();
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return super.listResponseResult();
    }


    @AppLogMethod(value = SysUser.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    @GetMapping("/exist")
    public ResponseBodyWrap<?> exist(SysUser sysUser) {
        Map<String, Object> map = JacksonUtils.object2HashMap2(objectMapper, sysUser);
        List<SysUser> sysUserList = domainService.list(map);
        return new ResponseBodyWrapBuilder().content(!sysUserList.isEmpty()).build();
    }


    @AppLogMethod(value = SysUser.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:add')")
    @PostMapping
    public ResponseBodyWrap<?> saveSysUserCheck(@Validated @RequestBody SysUser sysUser) throws AppException {
        String encodePassword = passwordEncoder.encode(sysUser.getPassword());
        sysUser.setPassword(encodePassword);
        return super.handleResponseBody(domainService.saveSysUserCheck(sysUser));
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:modify')")
    @PutMapping
    public ResponseBodyWrap<?> update(@RequestBody SysUser sysUser, @RequestParam(required = false, defaultValue = "000") String rootPwd) throws AppException {
        if (("admin".equalsIgnoreCase(sysUser.getUsername()) || "root".equalsIgnoreCase(sysUser.getUsername())) && !appProperties.getRootPassword().equalsIgnoreCase(rootPwd)) {
            throw new AppException("禁止修改 admin root 用户！");
        }
        return super.updateResponseResult(sysUser, Sets.newHashSet("nickname", "phone", "userStatus", "email"));
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:modify')")
    @PutMapping("/reset-password")
    public ResponseBodyWrap<?> restPassword(@RequestBody SysUser sysUser, @RequestParam String rawPassword, @RequestParam(required = false, defaultValue = "000") String rootPwd) throws AppException {
        if (("admin".equalsIgnoreCase(sysUser.getUsername()) || "root".equalsIgnoreCase(sysUser.getUsername())) && !appProperties.getRootPassword().equalsIgnoreCase(rootPwd)) {
            throw new AppException("禁止修改 admin root 用户！");
        }
        if (Strings.isNullOrEmpty(rawPassword)) {
            throw new AppException("新密码不能为空！");
        }
        sysUser.setPassword(passwordEncoder.encode(rawPassword));
        sysUser.setChangePasswordDateTime(LocalDateTime.now());
        return super.updateResponseResult(sysUser, Sets.newHashSet("password", "lastPasswordResetDate"));
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteSysUserCheck(@RequestParam Set<Long> ids) throws AppException {
        domainService.deleteSysUserCheck(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysUser.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(super.domainService.getByIdWithRole(id));
    }
}
