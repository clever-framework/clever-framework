package io.github.toquery.framework.oauth2.resource.server.autoconfig;


import io.github.toquery.framework.oauth2.resource.server.properties.AppOAuth2ResourceServerMultipleProperties;
import io.github.toquery.framework.oauth2.resource.server.properties.AppOAuth2RBACProperties;
import io.github.toquery.framework.oauth2.resource.server.properties.AppOAuth2ResourceServerOpaqueTokenProperties;
import io.github.toquery.framework.oauth2.resource.server.properties.AppOAuth2ResourceServerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({
        AppOAuth2RBACProperties.class,
        AppOAuth2ResourceServerProperties.class,
        AppOAuth2ResourceServerMultipleProperties.class,
        AppOAuth2ResourceServerOpaqueTokenProperties.class
})
public class AppAuth2ResourceServerAutoConfiguration {

    public AppAuth2ResourceServerAutoConfiguration() {
        log.info("自动装配 App OAuth2 Resource Server 自动化配置");
    }


}
