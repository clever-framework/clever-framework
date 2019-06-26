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
        return this.handleQuery(filterParam);
    }

    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    protected Page<E> handleQuery(Map<String, Object> filterParam) {
        //执行分页查询
        return this.handleQuery(filterParam, null);
    }

    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    protected Page<E> handleQuery(String[] sorts) {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return this.handleQuery(filterParam, sorts);
    }

    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    private Page<E> handleQuery(Map<String, Object> filterParam, String[] sorts) {
        //执行分页查询
        return service.queryByPage(filterParam, super.getRequestPageNumber(), super.getRequestPageSize(), sorts);
    }


    public ResponseParam query() {
        return ResponseParam.builder().build().page(this.handleQuery());
    }


    public ResponseParam query(String[] sorts) {
        return ResponseParam.builder().build().page(this.handleQuery(sorts));
    }

    protected ResponseParam query(Map<String, Object> filterParam, String[] sorts) {
        return ResponseParam.builder().build().page(this.handleQuery(filterParam, sorts));
    }


    protected List<E> handleList() {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return this.handleList(filterParam);
    }

    protected List<E> handleList(Map<String, Object> filterParam) {
        //执行分页查询
        return this.handleList(filterParam, null);
    }

    protected List<E> handleList(String[] sorts) {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return service.find(filterParam, sorts);
    }

    protected List<E> handleList(Map<String, Object> filterParam, String[] sorts) {
        //执行分页查询
        return service.find(filterParam, sorts);
    }

    public ResponseParam list() {
        return this.handleResponseParam(this.handleList());
    }

    public ResponseParam list(String[] sorts) {
        return this.handleResponseParam(this.handleList(sorts));
    }

    public ResponseParam list(Map<String, Object> filterParam, String[] sorts) {
        return this.handleResponseParam(this.handleList(filterParam, sorts));
    }


    protected ResponseParam handleResponseParam(Object object) {
        return ResponseParam.builder().build().content(object);
    }

    public E saveEntity(E entity) {
        return service.save(entity);
    }

    public ResponseParam save(E entity) {
        return this.handleResponseParam(this.saveEntity(entity));
    }

    public ResponseParam update(E entity, Set<String> updateEntityFields) {
        return this.handleResponseParam(service.update(entity, updateEntityFields));
    }

    public void delete(Set<ID> ids) {
        service.deleteByIds(ids);
    }


    public E getById(ID id) {
        return service.getById(id);
    }

    public ResponseParam detail(ID id) {
        return this.handleResponseParam(this.getById(id));
    }


}
