package io.github.toquery.framework.security.jwt;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.system.domain.SysUser;
import io.jsonwebtoken.Claims;
import io.toquery.framework.test.AppTestBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class JwtTokenTest extends AppTestBase {

    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil(new AppSecurityJwtProperties());

    @Before
    public void before() {
    }

    @Test
    public void generateToken() {
        SysUser userDetails = new SysUser();
        userDetails.setUsername("admin");
        String token = jwtTokenUtil.generateToken(userDetails);
        log.info("token: {}", token);
    }


    @Test
    public void doGenerateToken() {
        Map<String, Object> claims = Maps.newHashMap();
        claims.put("nickname", "张三");
        claims.put("roles", Sets.newHashSet("admin", "root", "menu"));
        String token = jwtTokenUtil.doGenerateToken(claims, "admin");
        log.info("token: {}", token);
    }

    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbImFkbWluIiwibWVudSIsInJvb3QiXSwibmlja25hbWUiOiLlvKDkuIkiLCJleHAiOjE1NjM0NDE2OTcsImlhdCI6MTU2MzQzODA5N30.22222rFelGE97L87RvBEdSVsGfWYgqZ8FcOsgjQbyxjTQJ237w42PyIhXoAjukWMJPJ8ge3KdOloUNCm2UwStl-YDDA";


    @Test
    public void jwtTokenUtil() {
        Claims claims = jwtTokenUtil.getAllClaimsFromToken(token);
        log.info(JSON.toJSONString(claims));
//        claims.
    }
}
