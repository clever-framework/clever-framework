package io.github.toquery.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.function.Supplier;

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
    public Supplier<SecurityContext> loadContext(HttpServletRequest request) {
        log.debug("loadContext");
        return SecurityContextRepository.super.loadContext(request);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        log.debug("saveContext");
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        log.debug("containsContext");
        return false;
    }
}
