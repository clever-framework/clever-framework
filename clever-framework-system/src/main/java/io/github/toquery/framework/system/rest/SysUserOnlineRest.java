package io.github.toquery.framework.system.rest;

import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.micrometer.core.annotation.Timed;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/sys/user/online")
@Timed(value = "system-user", description = "系统-用户在线")
public class SysUserOnlineRest extends AppBaseWebMvcController {
//
//    public static final String MODEL_NAME = "系统管理";
//
//    public static final String BIZ_NAME = "用户在线管理";
//
//    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
//    @PreAuthorize("hasAnyAuthority('system:user:online:query')")
//    @GetMapping
//    public ResponseBodyWrap<?> pageResponseResult() {
//        return super.pageResponseResult();
//    }
//
//    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
//    @PreAuthorize("hasAnyAuthority('system:user:online:query')")
//    @GetMapping("/list")
//    public ResponseBodyWrap<?> listResponseResult() {
//        return super.listResponseResult();
//    }
//
//    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
//    @PreAuthorize("hasAnyAuthority('system:user:online:delete')")
//    @DeleteMapping
//    public ResponseBodyWrap<?> deleteResponseResult(@RequestParam Set<Long> ids) throws AppException {
//        return super.deleteResponseResult(ids);
//    }
//
//    @AppLogMethod(value = SysUserOnline.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
//    @PreAuthorize("hasAnyAuthority('system:user:online:query')")
//    @GetMapping("{id}")
//    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
//        return super.detailResponseBody(id);
//    }
}
