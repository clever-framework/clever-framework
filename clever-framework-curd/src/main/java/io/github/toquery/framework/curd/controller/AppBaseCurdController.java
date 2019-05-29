package io.github.toquery.framework.curd.controller;

import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.web.controller.AppBaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.Map;

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

}
