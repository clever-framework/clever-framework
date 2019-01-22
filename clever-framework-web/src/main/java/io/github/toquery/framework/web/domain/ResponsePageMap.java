package io.github.toquery.framework.web.domain;

import io.github.toquery.framework.core.config.AppPageProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

/**
 * @author toquery
 * @version 1
 */
public class ResponsePageMap extends HashMap<String, Object> implements InitializingBean {


    @Autowired
    private AppPageProperties appPageProperties;

    private ResponsePageMap() {
    }

    public static ResponsePageMap getInstance() {
        return new ResponsePageMap();
    }

    private String pageSize = "";

    private String pageNumber = "";

    private String totalElements = "";

    private String totalPages = "";

    public ResponsePageMap pageSize(Integer pageSizeValue) {
        ResponsePageMap responsePageMap = new ResponsePageMap();
        responsePageMap.put(pageSize, pageSizeValue);
        return responsePageMap;
    }

    public static <T> ResponsePageMap build(com.github.pagehelper.Page<T> page) {
        ResponsePageMap responsePageMap = getInstance();

        ResponsePage responsePage = new ResponsePage();
        responsePage.setPageNumber(page.getPageNum());
        responsePage.setPageSize(page.getPageSize());
        responsePage.setTotalElements(page.getTotal());
        responsePage.setTotalPages(page.getPages());
        responsePageMap.put("page", responsePage);


        responsePageMap.put("content", page);
        return responsePageMap;
    }

    public static <T> ResponsePageMap build(org.springframework.data.domain.Page<T> page) {
        ResponsePageMap responsePageMap = getInstance();

        ResponsePage responsePage = new ResponsePage();
        responsePage.setPageNumber(page.getNumber());
        responsePage.setPageSize(page.getSize());
        responsePage.setTotalElements(page.getTotalElements());
        responsePage.setTotalPages(page.getTotalPages());
        responsePageMap.put("page", responsePage);


        responsePageMap.put("content", page.getContent());
        return responsePageMap;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pageSize = appPageProperties.getParam().getPageSize();

    }
}
