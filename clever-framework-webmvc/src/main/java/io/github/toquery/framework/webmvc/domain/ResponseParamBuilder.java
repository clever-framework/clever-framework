package io.github.toquery.framework.webmvc.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.PagedModel;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public final class ResponseParamBuilder {


    private boolean success;

    private int code;

    private String message;

    private ResponsePage page;

    private Object content;

    private Map<String, Object> param;

    public ResponseParamBuilder() {

    }

    public ResponseParamBuilder(ResponseParam responseParam) {
    }



    public ResponseParamBuilder code(int code) {
        this.code = code;
        return this;
    }

    public ResponseParamBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ResponseParamBuilder page(ResponsePage page) {
        this.page = page;
        return this;
    }

    public ResponseParamBuilder page(com.github.pagehelper.Page<?> page) {
        this.page =  new ResponsePageBuilder().page(page).build();
        this.content = page.getResult();
        return this;
    }

    public ResponseParamBuilder page(org.springframework.data.domain.Page<?> page) {
        this.page =  new ResponsePageBuilder().page(page).build();
        this.content = page.getContent();
        return this;
    }

    public ResponseParamBuilder page(PagedModel.PageMetadata pageMetadata) {
        this.page =  new ResponsePageBuilder().page(pageMetadata).build();
        return this;
    }

    public ResponseParamBuilder content(Object content) {
        this.content = content;
        return this;
    }

    public ResponseParamBuilder param(String key, Object param) {
        if (this.param == null) {
            this.param = new HashMap<>();
        }
        this.param.put(key, param);
        return this;
    }

    public ResponseParam build() {
        return new ResponseParam(this);
    }

    public ResponseParamBuilder fail() {
        this.message = "处理失败";
        this.success = false;
        return this;
    }

    public ResponseParamBuilder message() {
        this.message = "成功";
        this.success = true;
        return this;
    }
}
