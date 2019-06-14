package io.github.toquery.framework.curd.controller;

import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.webmvc.controller.AppBaseController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseCurdController<S extends AppBaseService<E, ID>, E, ID extends Serializable> extends AppBaseController {


    @Autowired
    protected S service;


    /**
     * 获取查询参数
     * @return 查询参数
     */
    protected Map<String, Object> getFilterParam() {
        return getParametersStartingWith(request, appWebProperties.getParam().getFilterPrefix());
    }


    /**
     * 处理分页和查询参数
     * @return 查询数据库后的数据
     */
    protected Page<E> handleQuery() {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return service.queryByPage(filterParam, super.getRequestPageNumber(), super.getRequestPageSize());
    }

    protected List<E> handleList() {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return service.find(filterParam);
    }


    public ResponseParam query() {
        return ResponseParam.builder().build().page(this.handleQuery());
    }

    public ResponseParam list() {
        return ResponseParam.builder().build().content(this.handleList());
    }

    public ResponseParam save(E entity) {
        return ResponseParam.builder().build().content(service.save(entity));
    }

    public ResponseParam update(E entity,Set<String> updateEntityFields) {
        return ResponseParam.builder().build().content(service.update(entity, updateEntityFields));
    }

    public void delete(Set<ID> ids) {
        service.deleteByIds(ids);
    }

    public ResponseParam detail(ID id) {
        return ResponseParam.builder().build().content(service.getById(id));
    }



}
