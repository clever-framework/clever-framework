package io.github.toquery.framework.oauth2.resource.server.properties;

import io.github.toquery.framework.oauth2.resource.server.constant.UserInfoCacheExpiredEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 */
@Slf4j
@Setter
@Getter
@ConfigurationProperties(prefix = "spring.security.oauth2.resourceserver.opaquetoken")
public class AppOAuth2ResourceServerOpaqueTokenProperties extends OAuth2ResourceServerProperties.Opaquetoken {

    private boolean userInfoEnabled = true;

    /**
     * 获取用户信息接口
     */
    private String userInfoUri;


    /**
     * 返回用户信息时填充的属性值，如果已存在则不会被覆盖
     */
    private Collection<String> userInfoAttributes = new HashSet<>();

    /**
     * 获取用户信息超时时间
     */
    private Duration userInfoTimeout = Duration.ofSeconds(5);

    /**
     * 获取用户信息超时时间
     */
    private Duration cacheExpired = Duration.ofHours(1);


    private boolean rbacEnabled = true;

    /**
     * 获取RBAC接口
     */
    private String rbacUri;


    private String rbacClientId;

    /**
     * 获取用户RBAC信息超时时间
     */
    private Duration rbacTimeout = Duration.ofSeconds(5);

    private String rbacCodePath;


}
