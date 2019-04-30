package io.github.toquery.framework.webmvc;

import io.github.toquery.framework.web.domain.ResponseParam;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseController<S>{

    @Autowired
    private S service;

    @Resource
    private HttpServletRequest request;

    protected ResponseParam list(){
        return null;
    }



}
