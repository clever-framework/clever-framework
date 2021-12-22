package io.github.toquery.framework.crud.controller;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import io.github.toquery.framework.webmvc.domain.ResponseBodyBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseCrudController<DS extends AppBaseService<E>, E> extends AppBaseWebMvcController {

    @Autowired
    protected DS doaminService;

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
    protected Page<E> page() {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return this.page(filterParam);
    }

    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    protected Page<E> page(Map<String, Object> filterParam) {
        //执行分页查询
        return this.page(filterParam, null);
    }

    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    protected Page<E> page(String[] sorts) {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return this.page(filterParam, sorts);
    }


    public ResponseResult pageResponseBody() {
        return new ResponseBodyBuilder().page(this.page()).build();
    }

    public ResponseResult pageResponseBody(Map<String, Object> filterParam) {
        return new ResponseBodyBuilder().page(this.page(filterParam)).build();
    }

    public ResponseResult pageResponseBody(String[] sorts) {
        return new ResponseBodyBuilder().page(this.page(sorts)).build();
    }

    protected ResponseResult pageResponseBody(Map<String, Object> filterParam, String[] sorts) {
        return new ResponseBodyBuilder().page(this.page(filterParam, sorts)).build();
    }


    protected List<E> list() {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return this.list(filterParam);
    }

    protected List<E> list(Map<String, Object> filterParam) {
        //执行分页查询
        return this.list(filterParam, null);
    }

    protected List<E> list(String[] sorts) {
        //获取查询参数
        Map<String, Object> filterParam = getFilterParam();
        //执行分页查询
        return this.list(filterParam, sorts);
    }

    public ResponseResult listResponseBody() {
        return this.handleResponseBody(this.list());
    }


    public ResponseResult listResponseBody(Map<String, Object> filterParam) {
        return this.handleResponseBody(this.list(filterParam));
    }

    public ResponseResult listResponseBody(String[] sorts) {
        return this.handleResponseBody(this.list(sorts));
    }

    public ResponseResult listResponseBody(Map<String, Object> filterParam, String[] sorts) {
        return this.handleResponseBody(this.list(filterParam, sorts));
    }


    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    protected Page<E> page(Map<String, Object> filterParam, String[] sorts) {
        //执行分页查询
        return doaminService.page(filterParam, super.getRequestCurrent(), super.getRequestPageSize(), sorts);
    }

    protected List<E> list(Map<String, Object> filterParam, String[] sorts) {
        //执行分页查询
        return doaminService.list(filterParam, sorts);
    }

    public E saveEntity(E entity) {
        return doaminService.save(entity);
    }

    public ResponseResult saveResponseBody(E entity) {
        return this.handleResponseBody(this.saveEntity(entity));
    }

    public ResponseResult updateResponseBody(E entity) {
        return this.handleResponseBody(doaminService.update(entity));
    }

    public ResponseResult updateResponseBody(E entity, Set<String> updateEntityFields) {
        return this.handleResponseBody(doaminService.update(entity, updateEntityFields));
    }

    public void delete(Set<Long> ids) {
        doaminService.deleteByIds(ids);
    }


    public E getById(Long id) {
        return doaminService.getById(id);
    }

    public ResponseResult detailResponseBody(Long id) {
        return this.handleResponseBody(this.getById(id));
    }


}
