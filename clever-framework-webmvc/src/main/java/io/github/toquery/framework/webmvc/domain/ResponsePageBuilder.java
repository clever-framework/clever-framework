package io.github.toquery.framework.webmvc.domain;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.hateoas.PagedModel;

import java.util.HashMap;

/**
 * 将不同类型的分页转为相同的page对象
 * 查询出当前页号+1，第一页为1 !
 *
 * TODO 可优化 InitializingBean
 *
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

    public static ResponsePage build(PagedModel.PageMetadata pageMetadata) {
        ResponsePage responsePage = new ResponsePage();
        responsePage.setPageNumber((int) pageMetadata.getNumber() + 1);
        responsePage.setPageSize((int) pageMetadata.getSize());
        responsePage.setTotalElements(pageMetadata.getTotalElements());
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
        if (page != null) {
            responsePage.setPageNumber(page.getPageNum() + 1);
            responsePage.setPageSize(page.getPageSize());
            responsePage.setTotalElements(page.getTotal());
            responsePage.setTotalPages(page.getPages());
        }
        return responsePage;
    }

    public static ResponsePage build(org.springframework.data.domain.Page<?> page) {
        ResponsePage responsePage = new ResponsePage();
        if (page != null) {
            responsePage.setPageNumber(page.getNumber() + 1);
            responsePage.setPageSize(page.getSize());
            responsePage.setTotalElements(page.getTotalElements());
            responsePage.setTotalPages(page.getTotalPages());
        }
        return responsePage;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }
}
