package io.github.toquery.framework.webmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@AllArgsConstructor
public class ResponsePage {

    private int pageSize;

    private int current;

    private Long totalElements;

    private int totalPages;

    public ResponsePage() {
    }

    public ResponsePage(ResponseResultBuilder builder) {
        this.pageSize = builder.getPageSize();
        this.current = builder.getCurrent();
        this.totalElements = builder.getTotalElements();
        this.totalPages = builder.getTotalPages();
    }

    public static ResponseResultBuilder builder() {
        return new ResponseResultBuilder();
    }

    public ResponseResultBuilder newBuilder() {
        return new ResponseResultBuilder(this);
    }


}
