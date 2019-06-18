package io.github.toquery.framework.curd.controller;

import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.webmvc.controller.AppBaseController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

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
     *
     * @return 查询参数
     */
    protected Map<String, Object> getFilterParam() {
        return getParametersStartingWith(request, appWebProperties.getParam().getFilterPrefix());
    }


    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    protected Page<E> handleQuery() {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return service.queryByPage(filterParam, super.getRequestPageNumber(), super.getRequestPageSize());
    }

    protected Page<E> handleQuery(String[] sorts) {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return service.queryByPage(filterParam, super.getRequestPageNumber(), super.getRequestPageSize(), sorts);
    }

    protected List<E> handleList() {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return service.find(filterParam);
    }

    protected List<E> handleList(String[] sorts) {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return service.find(filterParam,sorts);
    }

    protected ResponseParam handleResponseParam(Object object) {
        return ResponseParam.builder().build().content(object);
    }


    public ResponseParam query() {
        return ResponseParam.builder().build().page(this.handleQuery());
    }


    public ResponseParam query(String[] sorts) {
        return ResponseParam.builder().build().page(this.handleQuery(sorts));
    }

    public ResponseParam list(String[] sorts) {
        return ResponseParam.builder().build().content(this.handleList(sorts));
    }

    public ResponseParam list() {
        return ResponseParam.builder().build().content(this.handleList());
    }

    public ResponseParam save(E entity) {
        return ResponseParam.builder().build().content(service.save(entity));
    }

    public ResponseParam update(E entity, Set<String> updateEntityFields) {
        return ResponseParam.builder().build().content(service.update(entity, updateEntityFields));
    }

    public void delete(Set<ID> ids) {
        service.deleteByIds(ids);
    }

    public ResponseParam detail(ID id) {
        return ResponseParam.builder().build().content(service.getById(id));
    }


}
