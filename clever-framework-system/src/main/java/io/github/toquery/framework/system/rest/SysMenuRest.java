package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.service.ISysMenuService;
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
@RequestMapping("/sys/menu")
public class SysMenuRest extends AppBaseCrudController<ISysMenuService, SysMenu, Long> {
    private String[] sort = new String[]{"sortNum_desc"};

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
        List<SysMenu> sysMenuList = service.find(null, sort);
        // 将lits数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseParamBuilder().content(sysMenuList).build();
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = "$modelName", bizName = "$bizName")
    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysMenu sysMenu) {
        return super.handleResponseParam(service.saveMenu(sysMenu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.MODIFY, modelName = "$modelName", bizName = "$bizName")
    @PutMapping
    public ResponseParam update(@RequestBody SysMenu menu) {
        return super.update(menu, Sets.newHashSet("name", "code", "sortNum"));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.DELETE, modelName = "$modelName", bizName = "$bizName")
    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        service.deleteMenu(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
