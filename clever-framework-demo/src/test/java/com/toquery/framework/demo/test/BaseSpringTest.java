package com.toquery.framework.demo.test;

import com.toquery.framework.demo.DemoApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author toquery
 * @version 1
 */
@ActiveProfiles("local")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
// @WithMockUser 使用 WithMockUser 伪造 用户信息
//@WithMockUser(username="admin",roles={"USER","ADMIN"})
// @WithUserDetails 使用指定的Bean 查找 'admin' 用户的信息
@WithUserDetails("admin")
public class BaseSpringTest {
}
