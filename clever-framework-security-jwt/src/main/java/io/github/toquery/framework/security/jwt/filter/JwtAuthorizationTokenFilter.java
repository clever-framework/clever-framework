package io.github.toquery.framework.security.jwt.filter;

import com.google.common.base.Strings;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 检测请求header中token是否合法
 */
@Slf4j
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;
    private final JwtTokenHandler jwtTokenHandler;
    private final String tokenHeader;

    private final AppSecurityProperties appSecurityProperties;

    private final PathMatcher matcher = new AntPathMatcher();

    public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenHandler jwtTokenHandler, AppSecurityProperties appSecurityProperties, AppSecurityJwtProperties appSecurityJwtProperties) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenHandler = jwtTokenHandler;
        this.tokenHeader = appSecurityJwtProperties.getHeader();
        this.appSecurityProperties = appSecurityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (appSecurityProperties.getIgnoring().stream().anyMatch(item -> matcher.match(item, request.getRequestURI()))) {
            // 继续而不调用此过滤器...
            log.info("当前请求 {} 已被设为白名单", request.getRequestURI());
            chain.doFilter(request, response);
        } else {
            log.debug("正在处理认证请求 {}", request.getRequestURL());

            String token = request.getHeader(this.tokenHeader);
            if (Strings.isNullOrEmpty(token)) {
                log.debug("处理认证请求 {} 时未从 header 获取 {} 将从请求param中读取。", request.getRequestURL(), this.tokenHeader);
                String[] requestParam = request.getParameterValues(this.tokenHeader);
                if (requestParam != null && requestParam.length > 0 && !Strings.isNullOrEmpty(requestParam[0])) {
                    token = requestParam[0];
                }
            }

            String username = null;
            if (Strings.isNullOrEmpty(token)) {
                log.warn("couldn't find bearer string, will ignore the header");
            } else {
                if (token.startsWith("Bearer ") || token.startsWith("bearer ")) {
                    token = token.substring(7);
                }
                try {
                    username = jwtTokenHandler.getUsernameFromToken(token);
                } catch (IllegalArgumentException e) {
                    log.error("an error occured during getting username from token");
                    e.printStackTrace();
                } catch (ExpiredJwtException e) {
                    log.warn("the token is expired and not valid anymore");
                    e.printStackTrace();
                }
            }

            log.debug("checking authentication for user '{}'", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("security context was null, so authorizating user");

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenHandler.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authorizated user '{}', setting security context", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            chain.doFilter(request, response);
        }
    }
}
