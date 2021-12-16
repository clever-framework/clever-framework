package io.github.toquery.framework.webmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.PagedModel;

/**
 * 将不同类型的分页转为相同的page对象
 * 查询出当前页号+1，第一页为1 !
 * <p>
 * TODO 可优化 InitializingBean
 *
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@AllArgsConstructor
public final class ResponseResultBuilder {
    private int pageSize;
    private int current;
    private Long totalElements;
    private int totalPages;

    public ResponseResultBuilder() {
    }

    public ResponseResultBuilder(ResponsePage responsePage) {
        this.pageSize = responsePage.getPageSize();
        this.current = responsePage.getPageSize();
        this.totalElements = responsePage.getTotalElements();
        this.totalPages = responsePage.getTotalPages();
    }

    public ResponseResultBuilder(PagedModel<?> pagedResources) {
        this(pagedResources.getMetadata());
    }

    public ResponseResultBuilder(PagedModel.PageMetadata pageMetadata) {
        if (pageMetadata != null) {
            this.current = (int) pageMetadata.getNumber() + 1;
            this.pageSize = (int) pageMetadata.getSize();
            this.totalElements = pageMetadata.getTotalElements();
            this.totalPages = (int) pageMetadata.getTotalPages();
        }
    }

    /**
     * 将 Page helper 对象转化为Page对象
     *
     * @param page Page helper 对象
     */
    public ResponseResultBuilder(com.github.pagehelper.Page<?> page) {
        if (page != null) {
            this.current = page.getPageNum() + 1;
            this.pageSize = page.getPageSize();
            this.totalElements = page.getTotal();
            this.totalPages = page.getPages();
        }
    }

    public ResponseResultBuilder(org.springframework.data.domain.Page<?> page) {
        if (page != null) {
            this.current = page.getNumber() + 1;
            this.pageSize = page.getSize();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
        }
    }


    public ResponseResultBuilder page(PagedModel<?> pagedResources) {
        return this.page(pagedResources.getMetadata());
    }


    public ResponseResultBuilder page(PagedModel.PageMetadata pageMetadata) {
        if (pageMetadata != null) {
            this.current = (int) pageMetadata.getNumber() + 1;
            this.pageSize = (int) pageMetadata.getSize();
            this.totalElements = pageMetadata.getTotalElements();
            this.totalPages = (int) pageMetadata.getTotalPages();
        }
        return this;
    }

    /**
     * 将 Page helper 对象转化为Page对象
     *
     * @param page Page helper 对象
     */
    public ResponseResultBuilder page(com.github.pagehelper.Page<?> page) {
        if (page != null) {
            this.current = page.getPageNum() + 1;
            this.pageSize = page.getPageSize();
            this.totalElements = page.getTotal();
            this.totalPages = page.getPages();
        }
        return this;
    }

    public ResponseResultBuilder page(org.springframework.data.domain.Page<?> page) {
        if (page != null) {
            this.current = page.getNumber() + 1;
            this.pageSize = page.getSize();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
        }
        return this;
    }

    public ResponseResultBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ResponseResultBuilder current(int pageNum) {
        this.current = pageNum;
        return this;
    }

    public ResponseResultBuilder totalElements(Long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public ResponseResultBuilder totalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public ResponsePage build() {
        return new ResponsePage(this);
    }
}
