package io.github.toquery.framework.core.security;

import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class AppSecurityIgnoringHandler implements ServletContextAware {

    private String contextPath;

    @Nullable
    private ServletContext servletContext;

    @Autowired
    private Set<AppSecurityIgnoring> appSecurityIgnoringSet;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.contextPath = servletContext.getContextPath();
    }

    public Set<String> getIgnoringSet() {
        Set<String> ignoring = appSecurityIgnoringSet.stream().flatMap(appSecurityIgnoring -> appSecurityIgnoring.ignoring().stream().map(item -> contextPath + item)).collect(Collectors.toSet());
        log.debug("AppSecurityIgnoringHandler 获取到框架 Url Ignoring Set {} 个，分别为 {}", ignoring.size(), Joiner.on(",").skipNulls().join(ignoring));
        return ignoring;
    }

    public Set<String> getIgnoringSet(Set<String> ignoringSet) {
        ignoringSet = ignoringSet.stream().map(item -> contextPath + item).collect(Collectors.toSet());
        log.debug("AppSecurityIgnoringHandler 获取到 Url Ignoring Set {} 个，分别为 {}", ignoringSet.size(), Joiner.on(",").skipNulls().join(ignoringSet));
        return ignoringSet;
    }


}
