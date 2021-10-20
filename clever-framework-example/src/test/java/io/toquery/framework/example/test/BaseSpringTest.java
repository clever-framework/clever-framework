package io.toquery.framework.example.test;

import com.toquery.framework.example.CleverFrameworkDemoApplication;
import io.toquery.framework.test.AppTestSpringBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CleverFrameworkDemoApplication.class)
// @WithMockUser 使用 WithMockUser 伪造 用户信息
//@WithMockUser(username="admin",roles={"USER","ADMIN"})
// @WithUserDetails 使用指定的Bean 查找 'admin' 用户的信息
//@WithUserDetails(value = "admin")
public class BaseSpringTest extends AppTestSpringBase {

    @Resource
    private AuthenticationManager authenticationManager;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Before
    public void before() {
        String username = "admin";
        String encode = passwordEncoder.encode(username);
        log.info("用户名： {} 原密码：{} ,加密后密码：{}", username, username, encode);
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, username));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
