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

    /**
     * 获取用户信息接口
     */
    private String userInfoUri;


    /**
     * 返回用户信息时填充的属性值
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

    /**
     * 获取用户信息过期类型
     */
    private UserInfoCacheExpiredEnum cacheExpiredType = UserInfoCacheExpiredEnum.TOKEN;

}
