package io.github.toquery.framework.web.controller;

import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseWebController extends WebUtils {
    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;
}
