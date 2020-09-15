package io.github.toquery.framework.security.jwt.filter;

import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.utils.SpringSecurityUtils;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT令牌自动续约过滤器
 * <p>
 * 前提是客户端(浏览器)是基于Cookie的方式来存储JWT令牌(而不是localStorage)

@Slf4j
public class JwtTokenAutoRenewalFilter extends OncePerRequestFilter implements InitializingBean {

    @Autowired
    private AppSecurityJwtProperties jwtProperties;

    @Autowired
    private JwtTokenHandler jwtTokenHandler;

    private AntPathRequestMatcher loginRequestMatcher;

    public JwtTokenAutoRenewalFilter() {
        log.debug("初始化JWT令牌自动续约过滤器");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (!loginRequestMatcher.matches(request)) {
            String jwtToken = jwtTokenHandler.getJwtToken();
            if (jwtToken != null && jwtTokenHandler.isValid(jwtToken)) { //如果当前JWT令牌是有效的，则提前校验令牌的有效期，并根据情况自动续约
                UserDetails loginUser = SpringSecurityUtils.getCurrentAuthenticatedUser();
                if (jwtTokenHandler.needRenewal(jwtToken)) { //需要提前续约?
                    log.info("Automatically renewal the JWT token in advance by cookie：{}", "jwtToken");
                    Assert.notNull(loginUser, "No UserDetails found in current spring security context!");
                    // jwtTokenService.issueToken(request, response, loginUser);
                }
            }
        }
        filterChain.doFilter(request, response);
    }


    @Override
    public void afterPropertiesSet() throws ServletException {
        super.afterPropertiesSet();
        loginRequestMatcher = new AntPathRequestMatcher(jwtProperties.getPath().getLogout());
    }

    protected Claims getCurrentJwtToken(HttpServletRequest request) {
        return (Claims) request.getAttribute(JwtTokenAuthorizationFilter.JWT_TOKEN_REQUEST_ATTR_KEY);
    }
}
 */
