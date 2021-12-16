package io.github.toquery.framework.system.rest;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.util.AppTreeUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.service.ISysAreaService;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import io.github.toquery.framework.webmvc.domain.ResponseBodyBuilder;
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
public class SysAreaRest extends AppBaseCrudController<ISysAreaService, SysArea> {


    private String[] sort = new String[]{"sortNum_desc"};

    @GetMapping
    public ResponseResult pageResponseBody() {
        return super.pageResponseBody(sort);
    }

    @GetMapping(value = "/list")
    public ResponseResult listResponseBody() {
        return super.listResponseBody(sort);
    }

    @GetMapping("/tree")
    public ResponseResult tree() throws Exception {
        List<SysArea> sysMenuList = doaminService.find(null, sort);
        // 将lits数据转为tree
        sysMenuList = AppTreeUtil.getTreeData(sysMenuList);
        return new ResponseBodyBuilder().content(sysMenuList).build();
    }

    @PutMapping
    public ResponseResult updateResponseBody(@RequestBody SysArea sysArea) throws AppException {
        return super.handleResponseBody(doaminService.update(sysArea, Sets.newHashSet("name", "code")));
    }

    @DeleteMapping
    public void deleteSysRoleCheck(@RequestParam Set<Long> ids) throws AppException {
        doaminService.deleteByIds(ids);
    }

    @GetMapping("{id}")
    public ResponseResult detailResponseBody(@PathVariable Long id) {
        return super.detailResponseBody(id);
    }
}
