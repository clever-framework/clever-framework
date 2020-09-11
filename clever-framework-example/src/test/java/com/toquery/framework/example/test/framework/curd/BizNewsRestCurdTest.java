package com.toquery.framework.example.test.framework.curd;


import com.toquery.framework.example.constant.BizNewsShowStatus;
import com.toquery.framework.example.constant.QueryType;
import com.toquery.framework.example.entity.BizNews;
import com.toquery.framework.example.service.IBizNewsService;
import com.toquery.framework.example.test.BaseSpringTest;
import io.github.toquery.framework.common.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@DataJpaTest
//@BootstrapWith
//@WebMvcTest(BizNewsRest.class)
@AutoConfigureMockMvc
public class BizNewsRestCurdTest extends BaseSpringTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private IBizNewsService bizNewsService;

    @Test
    public void query() throws Exception {

        List<BizNews> newsList = bizNewsService.findJpa();

        mvc.perform(get("/biz-news")
                .param("queryType", QueryType.APP.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/biz-news")
                .param("queryType", QueryType.JPA.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/biz-news")
                .param("queryType", QueryType.MYBATIS.name()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void list() throws Exception {
        mvc.perform(get("/biz-news/list")
                .param("queryType", QueryType.APP.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/biz-news/list")
                .param("queryType", QueryType.JPA.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/biz-news/list")
                .param("queryType", QueryType.MYBATIS.name()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void save() throws Exception {
        BizNews bizNews = new BizNews();
        bizNews.setTitle("test-rest-save");
        bizNews.setShowStatus(BizNewsShowStatus.SHOW);

        mvc.perform(post("/biz-news")
                .contentType(MediaType.APPLICATION_JSON)
                .param("queryType", QueryType.APP.name())
                .content(JacksonUtils.object2String(bizNews))
        )
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/biz-news")
                .contentType(MediaType.APPLICATION_JSON)
                .param("queryType", QueryType.JPA.name())
                .content(JacksonUtils.object2String(bizNews))
        )
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/biz-news")
                .contentType(MediaType.APPLICATION_JSON)
                .param("queryType", QueryType.MYBATIS.name())
                .content(JacksonUtils.object2String(bizNews))
        )
                .andDo(print())
                .andExpect(status().isOk());
    }


}
