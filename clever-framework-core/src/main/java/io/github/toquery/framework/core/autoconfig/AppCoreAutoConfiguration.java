package io.github.toquery.framework.core.autoconfig;

import io.github.toquery.framework.core.properties.AppProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AppProperties.class)
@ConditionalOnProperty(prefix = AppProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppCoreAutoConfiguration {
}
