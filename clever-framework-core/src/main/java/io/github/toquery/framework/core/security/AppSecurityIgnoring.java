package io.github.toquery.framework.core.security;

import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * Spring 自带实现如下
 * @see org.springframework.security.config.annotation.web.builders.WebSecurity.IgnoredRequestConfigurer
 * @see WebSecurity.MvcMatchersIgnoredRequestConfigurer
 *
 * 设置的白名单， 最后放入 {@link  WebSecurity } 的 ignoredRequests 成员变量
 */
public interface AppSecurityIgnoring {

    public String[] ignoring();

}
