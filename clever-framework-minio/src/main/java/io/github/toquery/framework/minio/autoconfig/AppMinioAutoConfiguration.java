package io.github.toquery.framework.minio.autoconfig;

import io.github.toquery.framework.minio.properties.AppMinioProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AppMinioProperties.class)
public class AppMinioAutoConfiguration {

    public AppMinioAutoConfiguration(){
        log.info("开始自动装配 App Minio 自动化配置");
    }
}
