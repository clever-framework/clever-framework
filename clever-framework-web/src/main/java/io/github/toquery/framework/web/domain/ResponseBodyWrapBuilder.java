package io.github.toquery.framework.web.domain;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.PagedModel;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
public final class ResponseBodyWrapBuilder {

    private boolean success = true;

    private int code;

    private String message;

    private ResponsePage page;

    private Object content;

    private Map<String, Object> param;

    public ResponseBodyWrapBuilder() {

    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ResponseBodyWrapBuilder success(boolean success) {
        this.success = success;
        return this;
    }

    public ResponseBodyWrapBuilder code(int code) {
        this.code = code;
        return this;
    }

    public ResponseBodyWrapBuilder message(String message) {
        this.message = message;
        return this;
    }

    public ResponseBodyWrapBuilder page(ResponsePage page) {
        this.page = page;
        return this;
    }

    public ResponseBodyWrapBuilder page(com.github.pagehelper.Page<?> page) {
        this.page = new ResponsePageBuilder().page(page).build();
        this.content = page == null ? null : page.getResult();
        return this;
    }

    public ResponseBodyWrapBuilder page(org.springframework.data.domain.Page<?> page) {
        this.page = new ResponsePageBuilder().page(page).build();
        this.content = page == null ? null : page.getContent();
        return this;
    }

    public ResponseBodyWrapBuilder page(com.baomidou.mybatisplus.extension.plugins.pagination.Page<?> page) {
        this.page = new ResponsePageBuilder().page(page).build();
        this.content = page == null ? null : page.getRecords();
        return this;
    }

    public ResponseBodyWrapBuilder page(IPage<?> page) {
        this.page = new ResponsePageBuilder().page(page).build();
        this.content = page == null ? null : page.getRecords();
        return this;
    }

    public ResponseBodyWrapBuilder page(PagedModel.PageMetadata pageMetadata) {
        this.page = new ResponsePageBuilder().page(pageMetadata).build();
        return this;
    }

    public ResponseBodyWrapBuilder content(Object content) {
        this.content = content;
        return this;
    }

    public ResponseBodyWrapBuilder param(String key, Object param) {
        if (this.param == null) {
            this.param = new HashMap<>();
        }
        this.param.put(key, param);
        return this;
    }

    public ResponseBodyWrap<?> build() {
        return new ResponseBodyWrap<>(this);
    }

    public ResponseBodyWrapBuilder fail() {
        return this.fail("处理失败");
    }

    public ResponseBodyWrapBuilder fail(String message) {
        this.message = message;
        this.success = false;
        return this;
    }

    public ResponseBodyWrapBuilder success() {
        return this.success("成功");
    }

    public ResponseBodyWrapBuilder success(String message) {
        this.message = message;
        this.success = true;
        return this;
    }
}
