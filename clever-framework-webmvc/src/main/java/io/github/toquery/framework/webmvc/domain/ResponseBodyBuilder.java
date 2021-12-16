package io.github.toquery.framework.webmvc.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.PagedModel;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public final class ResponseBodyBuilder {

    private boolean success = true;

    private int code;

    private String message;

    private ResponsePage page;

    private Object content;

    private Map<String, Object> param;

    public ResponseBodyBuilder() {

    }

    public ResponseBodyBuilder(ResponseResult responseParam) {
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResponseBodyBuilder success(boolean success) {
        this.success = success;
        return this;
    }

    public ResponseBodyBuilder code(int code) {
        this.code = code;
        return this;
    }

    public ResponseBodyBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ResponseBodyBuilder page(ResponsePage page) {
        this.page = page;
        return this;
    }

    public ResponseBodyBuilder page(com.github.pagehelper.Page<?> page) {
        this.page =  new ResponseResultBuilder().page(page).build();
        this.content = page == null ? null : page.getResult();
        return this;
    }

    public ResponseBodyBuilder page(org.springframework.data.domain.Page<?> page) {
        this.page = new ResponseResultBuilder().page(page).build();
        this.content = page == null ? null : page.getContent();
        return this;
    }

    public ResponseBodyBuilder page(PagedModel.PageMetadata pageMetadata) {
        this.page =  new ResponseResultBuilder().page(pageMetadata).build();
        return this;
    }

    public ResponseBodyBuilder content(Object content) {
        this.content = content;
        return this;
    }

    public ResponseBodyBuilder param(String key, Object param) {
        if (this.param == null) {
            this.param = new HashMap<>();
        }
        this.param.put(key, param);
        return this;
    }

    public ResponseResult build() {
        return new ResponseResult(this);
    }

    public ResponseBodyBuilder fail() {
        this.message = "处理失败";
        this.success = false;
        return this;
    }

    public ResponseBodyBuilder success() {
        this.message = "成功";
        this.success = true;
        return this;
    }
}
