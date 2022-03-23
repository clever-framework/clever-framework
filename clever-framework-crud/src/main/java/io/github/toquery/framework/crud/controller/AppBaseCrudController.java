package io.github.toquery.framework.crud.controller;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.web.domain.ResponseBodyWrapBuilder;
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
    protected DS domainService;

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


    public ResponseBodyWrap<?> pageResponseResult() {
        return new ResponseBodyWrapBuilder().page(this.page()).build();
    }

    public ResponseBodyWrap<?> pageResponseResult(Map<String, Object> filterParam) {
        return new ResponseBodyWrapBuilder().page(this.page(filterParam)).build();
    }

    public ResponseBodyWrap<?> pageResponseResult(String[] sorts) {
        return new ResponseBodyWrapBuilder().page(this.page(sorts)).build();
    }

    protected ResponseBodyWrap<?> pageResponseResult(Map<String, Object> filterParam, String[] sorts) {
        return new ResponseBodyWrapBuilder().page(this.page(filterParam, sorts)).build();
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

    public ResponseBodyWrap<?> listResponseResult() {
        return this.handleResponseBody(this.list());
    }


    public ResponseBodyWrap<?> listResponseResult(Map<String, Object> filterParam) {
        return this.handleResponseBody(this.list(filterParam));
    }

    public ResponseBodyWrap<?> listResponseResult(String[] sorts) {
        return this.handleResponseBody(this.list(sorts));
    }

    public ResponseBodyWrap<?> listResponseResult(Map<String, Object> filterParam, String[] sorts) {
        return this.handleResponseBody(this.list(filterParam, sorts));
    }


    /**
     * 处理分页和查询参数
     *
     * @return 查询数据库后的数据
     */
    protected Page<E> page(Map<String, Object> filterParam, String[] sorts) {
        //执行分页查询
        return domainService.page(filterParam, super.getRequestCurrent(), super.getRequestPageSize(), sorts);
    }

    protected List<E> list(Map<String, Object> filterParam, String[] sorts) {
        //执行分页查询
        return domainService.list(filterParam, sorts);
    }

    public E saveEntity(E entity) {
        return domainService.save(entity);
    }

    public ResponseBodyWrap<?> saveResponseResult(E entity) {
        return this.handleResponseBody(this.saveEntity(entity));
    }

    public ResponseBodyWrap<?> updateResponseResult(E entity) {
        return this.handleResponseBody(domainService.update(entity));
    }

    public ResponseBodyWrap<?> updateResponseResult(E entity, Set<String> updateEntityFields) {
        return this.handleResponseBody(domainService.update(entity, updateEntityFields));
    }

    public ResponseBodyWrap<?> deleteResponseResult(Set<Long> ids) {
        domainService.deleteByIds(ids);
        return ResponseBodyWrap.builder().success().build();
    }


    public E getById(Long id) {
        return domainService.getById(id);
    }

    public ResponseBodyWrap<?> detailResponseBody(Long id) {
        return this.handleResponseBody(this.getById(id));
    }


}
