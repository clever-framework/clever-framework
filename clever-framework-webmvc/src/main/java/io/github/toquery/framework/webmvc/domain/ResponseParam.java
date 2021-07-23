package io.github.toquery.framework.webmvc.domain;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

/**
 * 响应参数购将，根据相应的类型，将数据填充到content
 *
 * @author toquery
 * @version 1
 */
public class ResponseParam extends HashMap<String, Object> { //implements InitializingBean {

    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_PARAM = "success";
    private static final String CODE_PARAM = "code";
    private static final String MESSAGE_PARAM = "message";
    private static final String CONTENT_PARAM_VALUE = "content";
    private static final String PAGE_PARAM_VALUE = "page";

    public ResponseParam() {
    }

    public ResponseParam(ResponseParamBuilder builder) {
        this.code(builder.getCode());
        this.message(builder.getMessage());
        this.content(builder.getContent());
        this.success(builder.getSuccess());
        this.page(builder.getPage());
        if (builder.getParam() != null) {
            builder.getParam().forEach(this::param);
        }
    }

    public static ResponseParamBuilder builder() {
        return new ResponseParamBuilder();
    }

    public ResponseParamBuilder newBuilder() {
        return new ResponseParamBuilder(this);
    }

    private ResponseParam success(boolean success){
        this.put(SUCCESS_PARAM, success);
        return this;
    }

    private ResponseParam content(Object content) {
        this.put(CONTENT_PARAM_VALUE, content);
        return this;
    }

    /**
     * 代码或标识码
     *
     * @param code
     * @return
     */
    private ResponseParam code(Object code) {
        this.put(CODE_PARAM, code);
        return this;
    }

    /**
     * 添加参数信息，key为message，多次调用addMessage方法会替换相应的key值
     *
     * @param value
     * @return
     */
    private ResponseParam message(String value) {
        this.put(MESSAGE_PARAM, value);
        return this;
    }

    private ResponseParam page(ResponsePage responsePage) {
        this.put(PAGE_PARAM_VALUE, responsePage);
        return this;
    }

    /**
     * 将Spring Page转化为响应数据，包含分页相关的参数
     *
     * @param page 分页信息
     * @return 包含分页相关的参数
     */
    private ResponseParam page(org.springframework.data.domain.Page<?> page) {
        this.put(PAGE_PARAM_VALUE, new ResponsePageBuilder(page).build());
        this.put(CONTENT_PARAM_VALUE, page == null ? null : page.getContent());
        return this;
    }

    private ResponseParam page(PagedModel<?> pagedResources) {
        this.put(PAGE_PARAM_VALUE, new ResponsePageBuilder(pagedResources).build());
        this.put(CONTENT_PARAM_VALUE, pagedResources.getContent());
        return this;
    }

    /**
     * 仅仅包含分页相关的参数
     *
     * @param page 分页信息
     * @return 包含分页相关的参数
     */
    private ResponseParam page(com.github.pagehelper.Page<?> page) {
        this.put(PAGE_PARAM_VALUE, new ResponsePageBuilder(page).build());
        this.put(CONTENT_PARAM_VALUE, page);
        return this;
    }

    /**
     * 添加参数信息，如果key和SUCCESS_PARAM或FAIL_PARAM_PARAM相同，则会替换相应的值
     *
     * @param key   参数信息的key
     * @param value 参数信息的value
     * @return 响应数据
     */
    private ResponseParam param(String key, Object value) {
        this.put(key, value);
        return this;
    }

    /**
     * 获取包含状态码的response实体对象，状态码默认为200
     *
     * @return ResponseEntity实体对象
     */
    public ResponseEntity<ResponseParam> getResponseEntity() {
        return getResponseEntity(HttpStatus.OK);
    }

    /**
     * 获取包含状态码的response实体对象
     *
     * @param httpStatus 状态码
     * @return 包含状态码的response实体
     */
    public ResponseEntity<ResponseParam> getResponseEntity(HttpStatus httpStatus) {
        return getResponseEntity(httpStatus, null);
    }

    /**
     * 获取包含状态码和内容类型的response实体对象
     *
     * @param httpStatus  状态码
     * @param contentType 内容类型
     * @return 包含状态码的response实体
     */
    public ResponseEntity<ResponseParam> getResponseEntity(HttpStatus httpStatus, MediaType contentType) {
        if (httpStatus == null) {
            httpStatus = HttpStatus.OK;
        }
        if (contentType == null) {
            contentType = MediaType.APPLICATION_JSON;
        }
        return ResponseEntity.status(httpStatus).contentType(contentType).body(this);
    }

}

