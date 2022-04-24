package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.micrometer.core.annotation.Timed;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/user/online")
@Timed(value = "system-user", description = "系统-用户在线")
public class SysUserOnlineRest extends AppBaseCrudController<ISysUserOnlineService, SysUserOnline> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "用户在线管理";

    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:online:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        return super.pageResponseResult();
    }

    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:online:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return super.listResponseResult();
    }

    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:online:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteResponseResult(@RequestParam Set<Long> ids) throws AppException {
        return super.deleteResponseResult(ids);
    }

    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:user:online:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return super.detailResponseBody(id);
    }
}
