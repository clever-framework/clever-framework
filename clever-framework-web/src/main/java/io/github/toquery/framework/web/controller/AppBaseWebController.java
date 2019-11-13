package io.github.toquery.framework.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseWebController extends WebUtils {
    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;
}
