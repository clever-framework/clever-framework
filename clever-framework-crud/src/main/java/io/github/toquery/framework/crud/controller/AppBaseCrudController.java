package io.github.toquery.framework.crud.controller;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.domain.ResponseParamBuilder;
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
public class AppBaseCrudController<S extends AppBaseService<E>, E> extends AppBaseWebMvcController {


    @Autowired
    protected S service;


    /**
     * 获取查询参数
     *
     * @return 查询参数
     */
    protected Map<String, Object> getFilterParam() {
        return getParametersStartingWith(request, appWebMvcProperties.getParam().getFilterPrefix());
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
    protected Page<E> handleQuery(Map<String, Object> filterParam, String[] sorts) {
        //执行分页查询
        return service.queryByPage(filterParam, super.getRequestCurrent(), super.getRequestPageSize(), sorts);
    }


    public ResponseParam query() {
        return new ResponseParamBuilder().page(this.handleQuery()).build();
    }

    public ResponseParam query(Map<String, Object> filterParam) {
        return new ResponseParamBuilder().page(this.handleQuery(filterParam)).build();
    }

    public ResponseParam query(String[] sorts) {
        return new ResponseParamBuilder().page(this.handleQuery(sorts)).build();
    }

    protected ResponseParam query(Map<String, Object> filterParam, String[] sorts) {
        return new ResponseParamBuilder().page(this.handleQuery(filterParam, sorts)).build();
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


    public ResponseParam list(Map<String, Object> filterParam) {
        return this.handleResponseParam(this.handleList(filterParam));
    }

    public ResponseParam list(String[] sorts) {
        return this.handleResponseParam(this.handleList(sorts));
    }

    public ResponseParam list(Map<String, Object> filterParam, String[] sorts) {
        return this.handleResponseParam(this.handleList(filterParam, sorts));
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

    public void delete(Set<Long> ids) {
        service.deleteByIds(ids);
    }


    public E getById(Long id) {
        return service.getById(id);
    }

    public ResponseParam detail(Long id) {
        return this.handleResponseParam(this.getById(id));
    }


}
