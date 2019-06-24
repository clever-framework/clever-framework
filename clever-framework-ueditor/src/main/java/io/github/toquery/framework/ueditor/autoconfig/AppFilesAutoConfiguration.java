package io.github.toquery.framework.ueditor.autoconfig;

import io.github.toquery.framework.ueditor.properties.AppUeditorProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@ComponentScan(basePackages = "io.github.toquery.framework.ueditor")
public class AppFilesAutoConfiguration {
    public AppFilesAutoConfiguration() {
        log.info("初始化 App Ueditor 自动配置");
    }
}
