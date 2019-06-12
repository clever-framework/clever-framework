package io.github.toquery.framework.webmvc.controller;

import com.google.common.base.Strings;
import io.github.toquery.framework.webmvc.config.AppWebProperties;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseController extends WebUtils {

    @Resource
    protected HttpServletRequest request;


    @Resource
    protected AppWebProperties appWebProperties;

    protected String getRequestParameterValue(String name) {
        String[] values = request.getParameterValues(name);
        if (values == null || values.length <= 0) {
            return null;
        }
        return values[0];
    }

    protected int getRequestPageSize() {
        String pageSize = this.getRequestParameterValue(appWebProperties.getParam().getPageSize());
        return Strings.isNullOrEmpty(pageSize) ? appWebProperties.getDefaultValue().getPageSize() : Integer.valueOf(pageSize);
    }

    protected int getRequestPageNumber() {
        String pageNumber = this.getRequestParameterValue(appWebProperties.getParam().getPageNumber());
        return Strings.isNullOrEmpty(pageNumber) ? appWebProperties.getDefaultValue().getPageNumber() : Integer.valueOf(pageNumber);
    }

}
