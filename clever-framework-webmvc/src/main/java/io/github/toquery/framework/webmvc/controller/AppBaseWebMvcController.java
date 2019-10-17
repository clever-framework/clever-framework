package io.github.toquery.framework.webmvc.controller;

import com.google.common.base.Strings;
import io.github.toquery.framework.web.controller.AppBaseWebController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
public class AppBaseWebMvcController extends AppBaseWebController {


    @Resource
    protected AppWebMvcProperties appWebMvcProperties;


    protected String getRequestParameterValue(String name) {
        String[] values = request.getParameterValues(name);
        if (values == null || values.length <= 0) {
            return null;
        }
        return values[0];
    }

    /**
     * 获取每页数量
     * @return  每页数量
     */
    protected int getRequestPageSize() {
        String pageSize = this.getRequestParameterValue(appWebMvcProperties.getParam().getPageSize());
        return Strings.isNullOrEmpty(pageSize) ? appWebMvcProperties.getDefaultValue().getPageSize() : Integer.parseInt(pageSize);
    }

    /**
     * 获取当前第几页，第一页为1（1 转换为 0）
     * @return  当前第几页
     */
    protected int getRequestPageNumber() {
        String pageNumber = this.getRequestParameterValue(appWebMvcProperties.getParam().getPageNumber());
        return Strings.isNullOrEmpty(pageNumber) ? appWebMvcProperties.getDefaultValue().getPageNumber() : Integer.parseInt(pageNumber) - 1;
    }


    protected ResponseParam handleResponseParam(Object object) {
        return ResponseParam.builder().build().content(object);
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
