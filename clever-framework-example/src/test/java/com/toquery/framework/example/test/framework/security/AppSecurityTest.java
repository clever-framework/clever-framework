package com.toquery.framework.example.test.framework.security;

import com.google.common.collect.Sets;
import com.toquery.framework.example.test.BaseSpringMvcTest;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author toquery
 * @version 1
 */

@Slf4j
public class AppSecurityTest extends BaseSpringMvcTest {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    /*@Before
    public void before() {
        String username = "admin";
        String encode = passwordEncoder.encode(username);
        log.info("用户名： {} 原密码：{} ,加密后密码：{}", username, username, encode);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, username));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }*/


    @Autowired
    private JwtTokenHandler jwtTokenHandler;


    @Autowired
    private UserDetailsService userDetailsService;

    public HttpHeaders buildAdminTokenHeader(){
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");
        String token = jwtTokenHandler.generateToken(userDetails);

        assertNotNull(token);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        return headers;
    }


    @Test
    public void testSecured() throws Exception {
        HttpHeaders headers = this.buildAdminTokenHeader();
        mockMvc.perform(get("/app/security/secured").headers(headers).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testSecured_no() throws Exception {
        mockMvc.perform(get("/app/security/secured").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }



    @Test
    public void testNormal() throws Exception {
        mockMvc.perform(get("/app/security/normal").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // @WithMockUser(username="admin",roles={"USER","ADMIN"})
    @Test
    public void testAdmin() throws Exception {
        HttpHeaders headers = this.buildAdminTokenHeader();

        mockMvc.perform(get("/app/security/admin").headers(headers).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testAdmin_no() throws Exception {
        mockMvc.perform(get("/app/security/admin").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void testAll() throws Exception {
        HttpHeaders headers = this.buildAdminTokenHeader();

        mockMvc.perform(get("/app/security/all").headers(headers).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }


}
