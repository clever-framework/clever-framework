package io.github.toquery.framework.system.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.system.entity.SysPost;
import io.github.toquery.framework.system.service.ISysPostService;
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
@RequestMapping("/sys/post")
@Timed(value = "system-post", description = "系统-岗位")
public class SysPostRest extends AppBaseWebMvcController {

    private static final String[] SORT = new String[]{"parentIds_asc", "sortNum_desc"};

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "岗位管理";

    private final ISysPostService sysPostService;

    @AppLogMethod(value = SysPost.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:post:query')")
    @GetMapping
    public ResponseBodyWrap<?> pageResponseResult() {
        Page<SysPost> sysPostPage = sysPostService.page(new Page<>(super.getRequestCurrent(), super.getRequestPageSize()));
        return new ResponseBodyWrapBuilder().page(sysPostPage).build();
    }

    @AppLogMethod(value = SysPost.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:post:query')")
    @GetMapping("/list")
    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(sysPostService.list());
    }

    @AppLogMethod(value = SysPost.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:post:query')")
    @GetMapping("/tree")
    public ResponseBodyWrap<?> tree(@RequestParam(required = false, defaultValue = "根节点") String rootName) throws Exception {
        List<SysPost> sysDeptList = Lists.newArrayList(new SysPost(0L, rootName));
        List<SysPost> childrenList = sysPostService.list();
        sysDeptList.addAll(childrenList);
        // 将lits数据转为tree
        sysDeptList = AppTreeUtil.getTreeData(sysDeptList);
        return new ResponseBodyWrapBuilder().content(sysDeptList).build();
    }

    @PreAuthorize("hasAnyAuthority('system:post:add')")
    @AppLogMethod(value = SysPost.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseBodyWrap<?> saveResponseResult(@Validated @RequestBody SysPost sysPost) {
        return super.handleResponseBody(sysPostService.save(sysPost));
    }

    @PreAuthorize("hasAnyAuthority('system:post:modify')")
    @AppLogMethod(value = SysPost.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseBodyWrap<?> updateResponseResult(@RequestBody SysPost sysPost) {
        return this.handleResponseBody(sysPostService.updateById(sysPost));
    }

    @PreAuthorize("hasAnyAuthority('system:post:delete')")
    @AppLogMethod(value = SysPost.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public ResponseBodyWrap<?> deleteResponseResult(@RequestBody Set<Long> ids) {
        sysPostService.removeBatchByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }

    @AppLogMethod(value = SysPost.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PreAuthorize("hasAnyAuthority('system:post:query')")
    @GetMapping("{id}")
    public ResponseBodyWrap<?> detailResponseBody(@PathVariable Long id) {
        return this.handleResponseBody(sysPostService.getById(id));
    }
}
