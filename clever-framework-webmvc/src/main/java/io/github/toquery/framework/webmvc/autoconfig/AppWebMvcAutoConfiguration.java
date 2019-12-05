package io.github.toquery.framework.webmvc.autoconfig;

import io.github.toquery.framework.webmvc.config.AppWebMvcConfig;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@Import(AppWebMvcConfig.class)
@ComponentScan(basePackages = "io.github.toquery.framework.webmvc")
@EnableConfigurationProperties(value = {AppWebMvcProperties.class})
public class AppWebMvcAutoConfiguration {

    public AppWebMvcAutoConfiguration() {
        log.info("初始化 App Web Mvc 自动化配置");
    }
}


