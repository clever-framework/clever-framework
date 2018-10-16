package io.github.toquery.framework.demo.test;

import io.github.toquery.framework.demo.DemoApplication;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author toquery
 * @version 1
 */
//@ActiveProfiles("dev")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class BaseSpringTest {
}
