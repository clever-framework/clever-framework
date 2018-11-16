package io.github.toquery.framework.web.domain;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;

/**
 * @author toquery
 * @version 1
 */
public class ResponseParam extends HashMap<String, Object> {

    private static final long serialVersionUID = 1L;


    public static final String SUCCESS_PARAM_VALUE = "success";

    public static final String MESSAGE_PARAM = "message";

    public static final String DATA_PARAM = "data";

    public static final String DATA_LIST_PARAM = "datalist";

    public static final String CODE_PARAM = "code";

    public static final String EXCEPTION_ID_PARAM = "exceptionid";

    /**
     * 默认支持jsonp协议
     */
    @Getter
    private boolean isSupportJSONP = true;

    private ResponseParam() {
    }

    /**
     * 构建ResponseParam全局属性信息
     *
     * @return
     */
    public static ResponseParam appProperties() {
        return new ResponseParam();
    }

    /**
     * 根据flag返回不同类型的结果信息
     *
     * @param flag
     * @return
     */
    public static ResponseParam info(boolean flag) {

        if (flag) {
            return success();
        }

        return fail();
    }

    /**
     * 是否对jsonp协议的支持，如果支持获取request中的参数callback参数值，并重构返回结果。
     *
     * @param isSupportJSONP
     * @return
     */
    public ResponseParam supportJSONP(boolean isSupportJSONP) {
        this.isSupportJSONP = isSupportJSONP;
        return this;
    }

    public static ResponseParam success() {
        return success(true);
    }

    public static ResponseParam fail() {
        return success(false);
    }

    /**
     * 设置返回的成功状态
     *
     * @param flag
     * @return
     */
    public static ResponseParam success(boolean flag) {
        ResponseParam successParam = new ResponseParam();
        successParam.put(SUCCESS_PARAM_VALUE, flag);
        return successParam;
    }

    public static ResponseParam updateSuccess() {
        ResponseParam successParam = success();
        successParam.message("更新成功");
        return successParam;
    }

    public static ResponseParam updateFail() {
        ResponseParam failParam = fail();
        failParam.message("更新失败");
        return failParam;
    }

    public static ResponseParam saveSuccess() {
        ResponseParam successParam = success();
        successParam.message("添加成功");
        return successParam;
    }

    public static ResponseParam saveFail() {
        ResponseParam failParam = fail();
        failParam.message("添加失败");
        return failParam;
    }

    public static ResponseParam deleteSuccess() {
        ResponseParam successParam = success();
        successParam.message("删除成功");
        return successParam;
    }

    public static ResponseParam deleteFail() {
        ResponseParam failParam = fail();
        failParam.message("删除失败");
        return failParam;
    }

    /**
     * 代码或标识码
     *
     * @param code
     * @return
     */
    public ResponseParam code(Object code) {
        this.put(CODE_PARAM, code);
        return this;
    }

    /**
     * 添加参数信息，key为message，多次调用addMessage方法会替换相应的key值
     *
     * @param value
     * @return
     */
    public ResponseParam message(Object value) {
        this.put(MESSAGE_PARAM, value);
        return this;
    }

    /**
     * 添加单个参数信息，key为data，多次调用addMessage方法会替换相应的key值
     *
     * @param value
     * @return
     */
    public ResponseParam data(Object value) {
        this.put(DATA_PARAM, value);
        return this;
    }

    /**
     * 添加单个参数列表信息，key为data，多次调用addMessage方法会替换相应的key值
     *
     * @param value 向datalist添加的数据
     * @return 参数信息
     */
    public ResponseParam datalist(Iterable<?> value) {
        this.put(DATA_LIST_PARAM, value);
        return this;
    }

    /**
     * 异常的id
     *
     * @param exceptionId 异常的id
     * @return 包含异常的id的参数
     */
    public ResponseParam exceptionId(Object exceptionId) {
        this.put(EXCEPTION_ID_PARAM, exceptionId);
        return this;
    }

    /**
     * 仅仅包含分页相关的参数
     *
     * @param page 分页信息
     * @return 包含分页相关的参数
     */
    public ResponseParam pageParam(Page<?> page) {

        //设置分页参数信息
        this.put("pagenum", page.getNumber());
        this.put("pagesize", page.getSize());
        this.put("pagerealsize", page.getNumberOfElements());
        this.put("totalelements", page.getTotalElements());
        this.put("totalpages", page.getTotalPages());

        return this;
    }

    /**
     * 添加参数信息，如果key和SUCCESS_PARAM或FAIL_PARAM_PARAM相同，则会替换相应的值
     *
     * @param key   参数信息的key
     * @param value 参数信息的value
     * @return 响应数据
     */
    public ResponseParam param(String key, Object value) {
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

