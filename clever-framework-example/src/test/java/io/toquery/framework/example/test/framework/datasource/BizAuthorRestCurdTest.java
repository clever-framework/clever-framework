package io.toquery.framework.example.test.framework.datasource;


import com.toquery.framework.example.bff.admin.author.info.service.BizAuthorService;
import com.toquery.framework.example.bff.admin.news.info.model.constant.QueryType;
import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import com.toquery.framework.example.modules.author.info.service.BizAuthorDomainService;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.toquery.framework.example.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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
//@WebMvcTest(BizAuthorRest.class)
@AutoConfigureMockMvc
public class BizAuthorRestCurdTest extends BaseSpringTest {


    @Autowired
    private MockMvc mvc;

    @MockBean
    private BizAuthorService bizAuthorService;

    @MockBean
    private BizAuthorDomainService bizAuthorDomainService;

    @Test
    public void query() throws Exception {

        List<BizAuthor> newsList = bizAuthorService.findJpa();

        mvc.perform(get("/admin/biz-author")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.APP.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/admin/biz-author")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.JPA.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/admin/biz-author")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.MYBATIS.name()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void list() throws Exception {
        mvc.perform(get("/admin/biz-author/list")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.APP.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/admin/biz-author/list")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.JPA.name()))
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(get("/admin/biz-author/list")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.MYBATIS.name()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    public void save() throws Exception {
        BizAuthor bizNews = new BizAuthor();
        bizNews.setAuthorName("test-rest-save");

        mvc.perform(post("/admin/biz-author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.APP.name())
                        .content(JacksonUtils.object2String(bizNews))
                )
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/admin/biz-author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.JPA.name())
                        .content(JacksonUtils.object2String(bizNews))
                )
                .andDo(print())
                .andExpect(status().isOk());

        mvc.perform(post("/admin/biz-author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .param("queryType", QueryType.MYBATIS.name())
                        .content(JacksonUtils.object2String(bizNews))
                )
                .andDo(print())
                .andExpect(status().isOk());
    }


}
