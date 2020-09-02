package io.github.toquery.framework.security.jwt;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.jwt.utils.JwtTokenUtil;
import io.github.toquery.framework.system.entity.SysUser;
import io.jsonwebtoken.Claims;
import io.toquery.framework.test.AppTestBase;
import io.toquery.framework.test.AppTestSpringBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@TestConfiguration
//@ContextConfiguration(classes = {JwtTokenTest.class})
public class JwtTokenTest extends AppTestSpringBase {

    @Resource
    private JwtTokenHandler jwtTokenHandler;

    @Before
    public void before() {
    }

    @Test
    public void generateToken() {
        SysUser userDetails = new SysUser();
        userDetails.setUsername("admin");
        String token = jwtTokenHandler.generateToken(userDetails,"");
        log.info("token: {}", token);
    }


    @Test
    public void doGenerateToken() {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("nickname", "张三");
        claims.put("roles", Sets.newHashSet("admin", "root", "menu"));
        String token = jwtTokenHandler.doGenerateToken(claims, "admin","");
        log.info("token: {}", token);
    }

    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbImFkbWluIiwibWVudSIsInJvb3QiXSwibmlja25hbWUiOiLlvKDkuIkiLCJleHAiOjE1NjM0NDE2OTcsImlhdCI6MTU2MzQzODA5N30.22222rFelGE97L87RvBEdSVsGfWYgqZ8FcOsgjQbyxjTQJ237w42PyIhXoAjukWMJPJ8ge3KdOloUNCm2UwStl-YDDA";


    @Test
    public void jwtTokenUtil() {
        Claims claims = JwtTokenUtil.getAllClaimsFromToken("", token);
        log.info(JacksonUtils.object2String(claims));
//        claims.
    }
}
