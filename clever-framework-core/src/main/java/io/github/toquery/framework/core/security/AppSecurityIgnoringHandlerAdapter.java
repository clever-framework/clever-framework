package io.github.toquery.framework.core.security;

import com.google.common.base.Joiner;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.web.context.ServletContextAware;

import jakarta.servlet.ServletContext;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 白名单处理适配器
 */
@Slf4j
public class AppSecurityIgnoringHandlerAdapter implements ServletContextAware {

    @Getter
    private String contextPath;

    @Getter
    private ServletContext servletContext;

    @Autowired
    private Set<AppSecurityIgnoring> appSecurityIgnoringSet;

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
        this.contextPath = servletContext.getContextPath();
    }

    /**
     * 获取所有实现接口的类，读取类内忽略的url
     */
    public Set<String> getIgnoringSet() {
        Set<String> ignoring = appSecurityIgnoringSet.stream().flatMap(appSecurityIgnoring -> appSecurityIgnoring.ignoring().stream().map(item -> contextPath + item)).collect(Collectors.toSet());
        log.debug("AppSecurityIgnoringHandler 获取到框架 Url Ignoring Set {} 个，分别为 {}", ignoring.size(), Joiner.on(",").skipNulls().join(ignoring));
        return ignoring;
    }

    /**
     * 读取类内的url，并添加传入的url-
     */
    public Set<String> allIgnoringSet(Set<String> ignoringSet) {
        Set<String> ignoring = this.getIgnoringSet();
        ignoring.addAll(ignoringSet);
        return ignoring;
    }

    /**
     * 将传入的url 填充 context path
     */
    public Set<String> fillContextPath(Set<String> ignoringSet) {
        ignoringSet = ignoringSet.stream().map(item -> contextPath + item).collect(Collectors.toSet());
        log.debug("AppSecurityIgnoringHandler 获取到 Url Ignoring Set {} 个，分别为 {}", ignoringSet.size(), Joiner.on(",").skipNulls().join(ignoringSet));
        return ignoringSet;
    }


}
