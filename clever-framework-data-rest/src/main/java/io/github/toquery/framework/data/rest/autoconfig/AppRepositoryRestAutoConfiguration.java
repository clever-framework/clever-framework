package io.github.toquery.framework.data.rest.autoconfig;

import io.github.toquery.framework.data.rest.annotation.EnableAppRepositoryRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@EnableAppRepositoryRest
// @ConditionalOnProperty(prefix = AppSecurityJwtProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppRepositoryRestAutoConfiguration {

    public AppRepositoryRestAutoConfiguration() {
        log.info("初始化 Data Rest 自动化配置");
    }


}
