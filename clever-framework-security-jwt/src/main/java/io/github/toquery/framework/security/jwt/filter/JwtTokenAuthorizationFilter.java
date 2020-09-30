package io.github.toquery.framework.security.jwt.filter;

import com.google.common.base.Strings;
import io.github.toquery.framework.core.security.AppSecurityIgnoringHandlerAdapter;
import io.github.toquery.framework.security.jwt.handler.JwtTokenHandler;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Set;

/**
 * 检测请求header中token是否合法
 */
@Slf4j
public class JwtTokenAuthorizationFilter extends OncePerRequestFilter {

    /**
     * 当前JWT令牌在请求Attribute中的Key
     */
    public static final String JWT_TOKEN_REQUEST_ATTR_KEY = JwtTokenAuthorizationFilter.class.getName() + ".JwtToken";


    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenHandler jwtTokenHandler;

    @Autowired
    private AppSecurityProperties appSecurityProperties;

    @Autowired
    private AppSecurityIgnoringHandlerAdapter appSecurityIgnoringHandler;

    private final PathMatcher matcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        Set<String> ignoring = appSecurityIgnoringHandler.allIgnoringSet(appSecurityProperties.getIgnoring());

        if (ignoring.stream().anyMatch(item -> matcher.match(item, request.getRequestURI()))) {
            // 继续而不调用此过滤器...
            log.info("当前请求 {} 已被设为白名单", request.getRequestURI());
            chain.doFilter(request, response);
        } else {
            log.debug("正在处理认证请求 {}", request.getRequestURL());

            String token = jwtTokenHandler.getJwtToken();

            String username = null;

            try {
                username = jwtTokenHandler.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                log.error("an error occured during getting username from token");
                e.printStackTrace();
            } catch (ExpiredJwtException e) {
                log.warn("the token is expired and not valid anymore");
                e.printStackTrace();
            }

            log.debug("checking authentication for user '{}'", username);
            if (!Strings.isNullOrEmpty(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("security context was null, so authorizating user");

                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                if (jwtTokenHandler.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    log.info("authorizated user '{}', setting security context", username);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    request.setAttribute(JWT_TOKEN_REQUEST_ATTR_KEY, jwtTokenHandler.getClaimsFromToken(token)); //设置到请求属性中去
                }
            }

            chain.doFilter(request, response);
        }
    }
}
