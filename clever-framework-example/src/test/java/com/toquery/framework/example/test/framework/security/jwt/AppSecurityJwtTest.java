package com.toquery.framework.example.test.framework.security.jwt;

import com.toquery.framework.example.test.BaseSpringMvcTest;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author toquery
 * @version 1
 */

@Slf4j
public class AppSecurityJwtTest extends BaseSpringMvcTest {

    @Autowired
    private AppSecurityJwtProperties appSecurityJwtProperties;


    @Test
    public void testLogin() throws Exception {
        Map<String,String> data = new HashMap<>();

        data.put(appSecurityJwtProperties.getParam().getUsername(), "admin");
        data.put(appSecurityJwtProperties.getParam().getPassword(), "admin");

        mockMvc.perform(post(appSecurityJwtProperties.getPath().getToken())
                .content(JacksonUtils.object2String(data))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk());
    }



    @Test
    public void testLogin_Fail() throws Exception {
        Map<String,String> data = new HashMap<>();

        data.put(appSecurityJwtProperties.getParam().getUsername(), "admin");
        data.put(appSecurityJwtProperties.getParam().getPassword(), "112233");

        mockMvc.perform(post(appSecurityJwtProperties.getPath().getToken())
                .content(JacksonUtils.object2String(data))
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

}
