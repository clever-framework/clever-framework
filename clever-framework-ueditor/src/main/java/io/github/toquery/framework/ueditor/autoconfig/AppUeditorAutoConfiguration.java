package io.github.toquery.framework.ueditor.autoconfig;

import io.github.toquery.framework.ueditor.UeditorConfigManager;
import io.github.toquery.framework.ueditor.properties.AppUeditorProperties;
import io.github.toquery.framework.ueditor.rest.AppUeditorRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties({AppUeditorProperties.class})
@ConditionalOnProperty(prefix = AppUeditorProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppUeditorAutoConfiguration {


    @Resource
    private HttpServletRequest request;

    @Resource
    private AppUeditorProperties appUeditorProperties;

    public AppUeditorAutoConfiguration() {
        log.info("初始化 App Ueditor 自动配置");
    }

    @Bean
    public AppUeditorRest getUeditorRest() {
        return new AppUeditorRest(request, getUeditorConfigManager(), appUeditorProperties);
    }

    @Bean
    public UeditorConfigManager getUeditorConfigManager() {
        return new UeditorConfigManager(appUeditorProperties);
    }


}
