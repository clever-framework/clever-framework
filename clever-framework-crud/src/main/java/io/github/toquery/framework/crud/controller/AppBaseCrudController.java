package io.github.toquery.framework.crud.controller;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import io.github.toquery.framework.web.domain.ResponseBody;
import io.github.toquery.framework.web.domain.ResponseBodyBuilder;
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


    public ResponseBody pageResponseResult() {
        return new ResponseBodyBuilder().page(this.page()).build();
    }

    public ResponseBody pageResponseResult(Map<String, Object> filterParam) {
        return new ResponseBodyBuilder().page(this.page(filterParam)).build();
    }

    public ResponseBody pageResponseResult(String[] sorts) {
        return new ResponseBodyBuilder().page(this.page(sorts)).build();
    }

    protected ResponseBody pageResponseResult(Map<String, Object> filterParam, String[] sorts) {
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

    public ResponseBody listResponseResult() {
        return this.handleResponseBody(this.list());
    }


    public ResponseBody listResponseResult(Map<String, Object> filterParam) {
        return this.handleResponseBody(this.list(filterParam));
    }

    public ResponseBody listResponseResult(String[] sorts) {
        return this.handleResponseBody(this.list(sorts));
    }

    public ResponseBody listResponseResult(Map<String, Object> filterParam, String[] sorts) {
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

    public ResponseBody saveResponseResult(E entity) {
        return this.handleResponseBody(this.saveEntity(entity));
    }

    public ResponseBody updateResponseResult(E entity) {
        return this.handleResponseBody(domainService.update(entity));
    }

    public ResponseBody updateResponseResult(E entity, Set<String> updateEntityFields) {
        return this.handleResponseBody(domainService.update(entity, updateEntityFields));
    }

    public ResponseBody deleteResponseResult(Set<Long> ids) {
        domainService.deleteByIds(ids);
        return ResponseBody.builder().success().build();
    }


    public E getById(Long id) {
        return domainService.getById(id);
    }

    public ResponseBody detailResponseBody(Long id) {
        return this.handleResponseBody(this.getById(id));
    }


}
