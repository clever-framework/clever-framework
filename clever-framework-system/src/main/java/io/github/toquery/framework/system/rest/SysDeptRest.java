package io.github.toquery.framework.system.rest;

import com.google.common.collect.Lists;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysDept;
import io.github.toquery.framework.system.service.ISysDeptService;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import io.github.toquery.framework.webmvc.domain.ResponseBodyBuilder;

import org.springframework.security.access.prepost.PreAuthorize;
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

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptRest extends AppBaseCrudController<ISysDeptService, SysDept> {

    private static final String[] SORT = new String[]{"parentIds_asc", "sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "部门管理";

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping
    public ResponseResult pageResponseBody() {
        return super.pageResponseBody(SORT);
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("/list")
    public ResponseResult listResponseBody() {
        return super.listResponseBody(SORT);
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("/tree")
    public ResponseResult tree(@RequestParam(required = false, defaultValue = "根节点") String rootName) throws Exception {
        List<SysDept> sysDeptList = Lists.newArrayList(new SysDept(0L, rootName));
        List<SysDept> childrenList = doaminService.find(super.getFilterParam(), SORT);
        sysDeptList.addAll(childrenList);
        // 将lits数据转为tree
        sysDeptList = AppTreeUtil.getTreeData(sysDeptList);
        return new ResponseBodyBuilder().content(sysDeptList).build();
    }

    @PreAuthorize("hasAnyAuthority('system:dept:add')")
    @AppLogMethod(value = SysDept.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseResult saveResponseBody(@Validated @RequestBody SysDept sysDept) {
        return super.handleResponseBody(doaminService.saveDept(sysDept));
    }

    @PreAuthorize("hasAnyAuthority('system:dept:modify')")
    @AppLogMethod(value = SysDept.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseResult updateResponseBody(@RequestBody SysDept sysDept) {
        return this.handleResponseBody(super.doaminService.updateDept(sysDept));
    }

    @PreAuthorize("hasAnyAuthority('system:dept:delete')")
    @AppLogMethod(value = SysDept.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        doaminService.deleteDept(ids);
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("{id}")
    public ResponseResult detailResponseBody(@PathVariable Long id) {
        return super.detailResponseBody(id);
    }
}
