package io.github.toquery.framework.system.rest;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.entity.SysUser;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/menu")
public class SysMenuRest extends AppBaseCrudController<ISysMenuService, SysMenu> {

    public static final String MODEL_NAME = "系统管理";

    public static final String BIZ_NAME = "用户管理";

    private String[] sort = new String[]{"parentIds_asc", "sortNum_desc"};

    @GetMapping
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseParam query() {
        return super.query(sort);
    }

    @GetMapping("/list")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseParam list() {
        return super.list(sort);
    }

    @GetMapping("/tree")
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseParam tree() throws Exception {
        List<SysMenu> sysMenuList = Lists.newArrayList(new SysMenu(0L, "根菜单", "root"));
        List<SysMenu> childrenList = service.find(super.getFilterParam(), sort);
        sysMenuList.addAll(childrenList);
        // 将lits数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseParamBuilder().content(sysMenuList).build();
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseParam save(@Validated @RequestBody SysMenu sysMenu) {
        return super.handleResponseParam(service.saveMenu(sysMenu));
    }

    @PutMapping
    @AppLogMethod(value = SysMenu.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    public ResponseParam update(@RequestBody SysMenu menu) {
        return this.handleResponseParam(super.service.updateMenu(menu));
    }

    @AppLogMethod(value = SysMenu.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        service.deleteMenu(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
