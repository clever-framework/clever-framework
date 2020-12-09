package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.properties.AppProperties;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.service.ISysAreaService;
import io.github.toquery.framework.system.service.ISysRoleService;
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

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/area")
public class SysAreaRest extends AppBaseCrudController<ISysAreaService, SysArea, Long> {


    private String[] sort = new String[]{"sortNum_desc"};

    @GetMapping
    public ResponseParam query() {
        return super.query(sort);
    }

    @GetMapping(value = "/list")
    public ResponseParam list() {
        return super.list(sort);
    }

    @GetMapping("/tree")
    public ResponseParam tree() throws Exception {
        List<SysArea> sysMenuList = service.find(null, sort);
        // 将lits数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseParamBuilder().content(sysMenuList).build();
    }

    @PutMapping
    public ResponseParam update(@RequestBody SysArea sysArea) throws AppException {
        return super.handleResponseParam(service.update(sysArea, Sets.newHashSet("name", "code")));
    }

    @DeleteMapping
    public void deleteSysRoleCheck(@RequestParam Set<Long> ids) throws AppException {
        service.deleteByIds(ids);
    }

    @GetMapping("{id}")
    public ResponseParam detail(@PathVariable Long id) {
        return super.detail(id);
    }
}
