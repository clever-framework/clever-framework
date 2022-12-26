package io.github.toquery.framework.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

/**
 *
 */
@Slf4j
public class AppSecurityContextRepository implements SecurityContextRepository {
    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        SecurityContext context = (SecurityContext) requestResponseHolder.getRequest().getAttribute(RequestAttributeSecurityContextRepository.DEFAULT_REQUEST_ATTR_NAME);
        return (context != null) ? context : SecurityContextHolder.createEmptyContext();
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        log.info("loadContext servletPath = {}", servletPath);
        return SecurityContextRepository.super.loadDeferredContext(request);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String servletPath = request.getServletPath();
        log.info("saveContext servletPath = {}", servletPath);
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String servletPath = request.getServletPath();
        log.info("containsContext servletPath = {}", servletPath);
        return false;
    }
}
