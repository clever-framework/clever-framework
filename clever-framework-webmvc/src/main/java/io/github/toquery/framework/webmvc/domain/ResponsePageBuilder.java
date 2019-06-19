package io.github.toquery.framework.webmvc.domain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.hateoas.PagedResources;

import java.util.HashMap;

/**
 * @author toquery
 * @version 1
 */
public class ResponsePageBuilder extends HashMap<String, Object> implements InitializingBean {


    private ResponsePageBuilder() {
    }

    public static final ResponsePageBuilder instance = getInstance();

    /**
     * 获取构建实例
     *
     * @return 构建实例
     */
    public static ResponsePageBuilder getInstance() {
        return new ResponsePageBuilder();
    }

    public static ResponsePage build(PagedResources.PageMetadata pageMetadata) {
        ResponsePage responsePage = new ResponsePage();
        responsePage.setPageNumber((int) pageMetadata.getNumber());
        responsePage.setPageSize((int) pageMetadata.getSize());
        responsePage.setTotalElements((int) pageMetadata.getTotalElements());
        responsePage.setTotalPages((int) pageMetadata.getTotalPages());
        return responsePage;
    }

    /**
     * 将Pagehelper 对象转化为Page对象
     *
     * @param page Pagehelper对象
     * @return PageParam对象
     */
    public static ResponsePage build(com.github.pagehelper.Page<?> page) {
        ResponsePage responsePage = new ResponsePage();
        responsePage.setPageNumber(page.getPageNum());
        responsePage.setPageSize(page.getPageSize());
        responsePage.setTotalElements((int) page.getTotal());
        responsePage.setTotalPages(page.getPages());
        return responsePage;
    }

    public static ResponsePage build(org.springframework.data.domain.Page<?> page) {
        ResponsePage responsePage = new ResponsePage();
        responsePage.setPageNumber(page.getNumber());
        responsePage.setPageSize(page.getSize());
        responsePage.setTotalElements((int) page.getTotalElements());
        responsePage.setTotalPages(page.getTotalPages());
        return responsePage;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
