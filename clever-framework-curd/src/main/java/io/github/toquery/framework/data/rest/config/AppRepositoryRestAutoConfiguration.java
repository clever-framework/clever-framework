package io.github.toquery.framework.data.rest.config;

import io.github.toquery.framework.data.rest.annotation.EnableAppRepositoryRest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@EnableAppRepositoryRest
public class AppRepositoryRestAutoConfiguration {

    public AppRepositoryRestAutoConfiguration() {
        log.info("初始化 Data Rest 自动化配置");
    }


}
