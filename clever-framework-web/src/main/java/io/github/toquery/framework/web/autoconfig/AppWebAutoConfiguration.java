package io.github.toquery.framework.web.autoconfig;

import io.github.toquery.framework.web.properties.AppWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
@EnableConfigurationProperties({AppWebProperties.class})
@ConditionalOnProperty(prefix = AppWebProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppWebAutoConfiguration {
    public AppWebAutoConfiguration() {
        log.info("初始化 App Web 自动配置");
    }
}
