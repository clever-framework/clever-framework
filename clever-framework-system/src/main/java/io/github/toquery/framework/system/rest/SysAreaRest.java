package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysConfig;
import io.github.toquery.framework.system.service.ISysAreaService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.micrometer.core.annotation.Timed;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/sys/area")
@Timed(value = "system-area", description = "系统-行政区")
public class SysAreaRest extends AppBaseCrudController<ISysAreaService, SysArea> {


    private static final String[] sort = new String[]{"sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "行政区管理";


    @AppLogMethod(value = SysArea.class, logType = AppLogType.QUERY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:area:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        return super.pageResponseResult(sort);
    }

    @AppLogMethod(value = SysArea.class, logType = AppLogType.QUERY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:area:query')")
    @GetMapping(value = "/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return super.listResponseResult(sort);
    }

    @AppLogMethod(value = SysArea.class, logType = AppLogType.QUERY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:area:query')")
    @GetMapping("/tree")
    public ResponseBodyWrap<?> tree() throws Exception {
        List<SysArea> sysMenuList = domainService.list(null, sort);
        // 将lists数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseBodyWrapBuilder().content(sysMenuList).build();
    }

    @AppLogMethod(value = SysArea.class, logType = AppLogType.MODIFY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:area:modify')")
    @PutMapping
    public ResponseBodyWrap<?> updateResponseResult(@RequestBody SysArea sysArea) throws AppException {
        return super.handleResponseBody(domainService.update(sysArea, Sets.newHashSet("name", "code")));
    }

    @AppLogMethod(value = SysArea.class, logType = AppLogType.DELETE, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:area:delete')")
    @DeleteMapping
    public ResponseBodyWrap<?> deleteSysRoleCheck(@RequestParam Set<Long> ids) throws AppException {
        domainService.deleteByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysArea.class, logType = AppLogType.QUERY, modelName =  MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:area:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return super.detailResponseBody(id);
    }
}
