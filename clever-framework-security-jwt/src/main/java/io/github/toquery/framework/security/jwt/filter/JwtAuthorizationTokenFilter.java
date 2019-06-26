package io.github.toquery.framework.security.jwt.filter;

import com.google.common.base.Strings;
import io.github.toquery.framework.security.jwt.JwtTokenUtil;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
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
 * <p>
 */
@Slf4j
@Component
public class JwtAuthorizationTokenFilter extends OncePerRequestFilter {


    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;
    private final String tokenHeader;

    private final AppSecurityProperties appSecurityProperties;

    private final PathMatcher matcher = new AntPathMatcher();

    public JwtAuthorizationTokenFilter(UserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil, AppSecurityProperties appSecurityProperties, AppSecurityJwtProperties appSecurityJwtProperties) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.tokenHeader = appSecurityJwtProperties.getHeader();
        this.appSecurityProperties = appSecurityProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        if (appSecurityProperties.getWhitelist().stream().anyMatch(item -> matcher.match(item, request.getRequestURI()))) {
            // 继续而不调用此过滤器...
            log.info("当前请求 {} 已被设为白名单", request.getRequestURI());
            chain.doFilter(request, response);
        } else {
            log.debug("processing authentication for '{}'", request.getRequestURL());

            String token = request.getHeader(this.tokenHeader);
            String[] requestParam = request.getParameterValues(this.tokenHeader);
            if (Strings.isNullOrEmpty(token)) {
                if (requestParam != null && requestParam.length > 0 && !Strings.isNullOrEmpty(requestParam[0])) {
                    token = requestParam[0];
                }
            }

            String username = null;
            String authToken = null;
            if (token != null && token.startsWith("Bearer ")) {
                authToken = token.substring(7);
                try {
                    username = jwtTokenUtil.getUsernameFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    log.error("an error occured during getting username from token", e);
                } catch (ExpiredJwtException e) {
                    log.warn("the token is expired and not valid anymore", e);
                }
            } else {
                log.warn("couldn't find bearer string, will ignore the header");
            }

            log.debug("checking authentication for user '{}'", username);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.debug("security context was null, so authorizating user");

                // It is not compelling necessary to load the use details from the database. You could also store the information
                // in the token and read it from it. It's up to you ;)
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

                // For simple validation it is completely sufficient to just check the token integrity. You don't have to call
                // the database compellingly. Again it's up to you ;)
                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
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
