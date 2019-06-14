package io.github.toquery.framework.front.autoconfig;

import io.github.toquery.framework.front.connfig.AppFrontConfigurer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Import(AppFrontConfigurer.class)
@ComponentScan(basePackages = "io.github.toquery.framework.front")
public class AppFrontAutoConfig {
    public AppFrontAutoConfig() {
        log.info("初始化 App Front 自动配置");
    }
}
