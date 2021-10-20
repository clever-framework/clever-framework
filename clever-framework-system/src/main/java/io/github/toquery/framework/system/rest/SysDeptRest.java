package io.github.toquery.framework.system.rest;

import com.google.common.collect.Lists;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysDept;
import io.github.toquery.framework.system.service.ISysDeptService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.domain.ResponseParamBuilder;
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
    private String[] sort = new String[]{"parentIds_asc", "sortNum_desc"};

    @GetMapping
    public ResponseParam query() {
        return super.query(sort);
    }

    @GetMapping("/list")
    public ResponseParam list() {
        return super.list(sort);
    }

    @GetMapping("/tree")
    public ResponseParam tree() throws Exception {
        List<SysDept> sysDeptList = Lists.newArrayList(new SysDept(0L, "根节点"));
        List<SysDept> childrenList = service.find(super.getFilterParam(), sort);
        sysDeptList.addAll(childrenList);
        // 将lits数据转为tree
        sysDeptList = AppTreeUtil.getTreeData(sysDeptList);
        return new ResponseParamBuilder().content(sysDeptList).build();
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.CREATE, modelName = "$modelName", bizName = "$bizName")
    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysDept sysDept) {
        return super.handleResponseParam(service.saveDept(sysDept));
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.MODIFY, modelName = "$modelName", bizName = "$bizName")
    @PutMapping
    public ResponseParam update(@RequestBody SysDept sysDept) {
        return this.handleResponseParam(super.service.updateDept(sysDept));
    }

    @AppLogMethod(value = SysDept.class, logType = AppLogType.DELETE, modelName = "$modelName", bizName = "$bizName")
    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        service.deleteDept(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
