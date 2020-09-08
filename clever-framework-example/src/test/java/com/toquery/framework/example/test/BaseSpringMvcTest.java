package com.toquery.framework.example.test;

import com.toquery.framework.example.CleverFrameworkDemoApplication;
import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.data.rest.annotation.EnableAppRepositoryRest;
import io.toquery.framework.test.AppTestSpringBase;
import io.toquery.framework.test.AppTestSpringMvcBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.Resource;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@EnableAppRepositoryRest
@EnableAppJpaRepositories(basePackages = "com.toquery.framework.example.dao")
@SpringBootTest(classes = CleverFrameworkDemoApplication.class)
@AutoConfigureMockMvc
// @WithMockUser 使用 WithMockUser 伪造 用户信息
//@WithMockUser(username="admin",roles={"USER","ADMIN"})
// @WithUserDetails 使用指定的Bean 查找 'admin' 用户的信息
//@WithUserDetails(value = "admin")
public class BaseSpringMvcTest extends AppTestSpringMvcBase {

    @Autowired
    protected WebApplicationContext context;

    @Autowired
    protected MockMvc mockMvc;


//
//    @Before
//    public void setup() {
//        log.info("12");
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(context)
//                .apply(springSecurity())
//                .build();
//    }


}
