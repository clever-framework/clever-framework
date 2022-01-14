package io.github.toquery.framework.webmvc.domain;

import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

/**
 * 响应参数购将，根据相应的类型，将数据填充到content
 *
 * @author toquery 结果
 * @version 1
 */
public class ResponseResult extends HashMap<String, Object> { //implements InitializingBean {

    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_PARAM = "success";
    private static final String CODE_PARAM = "code";
    private static final String MESSAGE_PARAM = "message";
    private static final String CONTENT_PARAM_VALUE = "content";
    private static final String PAGE_PARAM_VALUE = "page";

    public ResponseResult() {
    }

    public ResponseResult(ResponseBodyBuilder builder) {
        this.setCode(builder.getCode());
        this.setMessage(builder.getMessage());
        this.setContent(builder.getContent());
        this.setSuccess(builder.getSuccess());
        this.setPage(builder.getPage());
        if (builder.getParam() != null) {
            builder.getParam().forEach(this::param);
        }
    }

    public static ResponseBodyBuilder builder() {
        return new ResponseBodyBuilder();
    }

    public ResponseBodyBuilder newBuilder() {
        return new ResponseBodyBuilder(this);
    }

    public void setSuccess(boolean success) {
        this.put(SUCCESS_PARAM, success);
    }

    public void setContent(Object content) {
        this.put(CONTENT_PARAM_VALUE, content);
    }

    public Object getContent() {
        return this.get(CONTENT_PARAM_VALUE);
    }

    /**
     * 代码或标识码
     *
     * @param code
     * @return
     */
    public void setCode(Integer code) {
        this.put(CODE_PARAM, code);
    }

    public Integer getCode() {
        Object value = this.get(CODE_PARAM);
        if (value == null) {
            return null;
        }
        return (Integer) value;
    }

    /**
     * 添加参数信息，key为message，多次调用addMessage方法会替换相应的key值
     *
     * @param value
     * @return
     */
    public void setMessage(String value) {
        this.put(MESSAGE_PARAM, value);
    }

    public String getMessage() {
        Object value = this.get(MESSAGE_PARAM);
        if (value == null) {
            return null;
        }
        return (String) value;
    }

    public void setPage(ResponsePage responsePage) {
        this.put(PAGE_PARAM_VALUE, responsePage);
    }

    public ResponsePage getPage() {
        Object value = this.get(PAGE_PARAM_VALUE);
        if (value == null) {
            return null;
        }
        return (ResponsePage) value;
    }

    /**
     * 将Spring Page转化为响应数据，包含分页相关的参数
     *
     * @param page 分页信息
     * @return 包含分页相关的参数
     */
    public ResponseResult page(org.springframework.data.domain.Page<?> page) {
        this.put(PAGE_PARAM_VALUE, new ResponseResultBuilder(page).build());
        this.put(CONTENT_PARAM_VALUE, page == null ? null : page.getContent());
        return this;
    }

    private ResponseResult page(PagedModel<?> pagedResources) {
        this.put(PAGE_PARAM_VALUE, new ResponseResultBuilder(pagedResources).build());
        this.put(CONTENT_PARAM_VALUE, pagedResources.getContent());
        return this;
    }

    /**
     * 仅仅包含分页相关的参数
     *
     * @param page 分页信息
     * @return 包含分页相关的参数
     */
    private ResponseResult page(com.github.pagehelper.Page<?> page) {
        this.put(PAGE_PARAM_VALUE, new ResponseResultBuilder(page).build());
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
    private ResponseResult param(String key, Object value) {
        this.put(key, value);
        return this;
    }

    /**
     * 获取包含状态码的response实体对象，状态码默认为200
     *
     * @return ResponseEntity实体对象
     */
    public ResponseEntity<ResponseResult> getResponseEntity() {
        return getResponseEntity(HttpStatus.OK);
    }

    /**
     * 获取包含状态码的response实体对象
     *
     * @param httpStatus 状态码
     * @return 包含状态码的response实体
     */
    public ResponseEntity<ResponseResult> getResponseEntity(HttpStatus httpStatus) {
        return getResponseEntity(httpStatus, null);
    }

    /**
     * 获取包含状态码和内容类型的response实体对象
     *
     * @param httpStatus  状态码
     * @param contentType 内容类型
     * @return 包含状态码的response实体
     */
    public ResponseEntity<ResponseResult> getResponseEntity(HttpStatus httpStatus, MediaType contentType) {
        if (httpStatus == null) {
            httpStatus = HttpStatus.OK;
        }
        if (contentType == null) {
            contentType = MediaType.APPLICATION_JSON;
        }
        return ResponseEntity.status(httpStatus).contentType(contentType).body(this);
    }

}

