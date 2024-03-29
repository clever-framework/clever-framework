package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.system.entity.SysDept;
import io.github.toquery.framework.system.service.ISysDeptService;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys/dept")
@Timed(value = "system-dept", description = "系统-部门")
public class SysDeptRest extends AppBaseWebMvcController {

    private static final String[] SORT = new String[]{"parentIds_asc", "sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "部门管理";

    private final ISysDeptService sysDeptService;

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysDept> sysDeptPage = sysDeptService.page(new Page<>(super.getRequestCurrent(), super.getRequestPageSize()));
        return new ResponseBodyWrapBuilder().page(sysDeptPage).build();
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysDeptService.list());
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("/tree")
    public ResponseBodyWrap<?> tree(@RequestParam(required = false, defaultValue = "根节点") String rootName) throws Exception {
        List<SysDept> sysDeptList = Lists.newArrayList(new SysDept(0L, rootName));
        List<SysDept> childrenList = sysDeptService.list();
        sysDeptList.addAll(childrenList);
        // 将lits数据转为tree
        sysDeptList = AppTreeUtil.getTreeData(sysDeptList);
        return new ResponseBodyWrapBuilder().content(sysDeptList).build();
    }

    @PreAuthorize("hasAnyAuthority('system:dept:add')")
    @AppLogMethod(value = SysDept.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseBodyWrap<?> saveResponseResult(@Validated @RequestBody SysDept sysDept) {
        return super.handleResponseBody(sysDeptService.saveDept(sysDept));
    }

    @PreAuthorize("hasAnyAuthority('system:dept:modify')")
    @AppLogMethod(value = SysDept.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseBodyWrap<?> updateResponseResult(@RequestBody SysDept sysDept) {
        return this.handleResponseBody(sysDeptService.updateDept(sysDept));
    }

    @PreAuthorize("hasAnyAuthority('system:dept:delete')")
    @AppLogMethod(value = SysDept.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public ResponseBodyWrap<?> deleteResponseResult(@RequestBody Set<Long> ids) {
        sysDeptService.deleteDept(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:dept:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return new ResponseBodyWrapBuilder().content(sysDeptService.getById(id)).build();
    }
}
