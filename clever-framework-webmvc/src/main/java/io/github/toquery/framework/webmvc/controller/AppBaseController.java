package io.github.toquery.framework.webmvc.controller;

import com.google.common.base.Strings;
import io.github.toquery.framework.webmvc.properties.AppWebProperties;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.WebUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseController extends WebUtils {

    @Resource
    protected HttpServletRequest request;

    @Resource
    protected HttpServletResponse response;

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

    protected ResponseEntity responseEntity(ResponseParam responseParam, HttpStatus httpStatus) {
        return new ResponseEntity<>(responseParam, httpStatus);
    }

    protected ResponseEntity responseEntity(String message, HttpStatus httpStatus) {
        ResponseParam responseParam = ResponseParam.builder().build().message(message);
        return this.responseEntity(responseParam, httpStatus);
    }

    protected ResponseEntity notFound(String message) {
        return this.responseEntity(message, HttpStatus.NOT_FOUND);
    }

    protected ResponseEntity notFound() {
        return this.notFound("未找到");
    }

}
