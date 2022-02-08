package io.github.toquery.framework.minio.autoconfig;

import io.github.toquery.framework.minio.properties.AppMinioProperties;
import io.github.toquery.framework.minio.rest.AppMinioRest;
import io.github.toquery.framework.minio.service.AppMinioService;
import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AppMinioProperties.class)
@ConditionalOnProperty(prefix = AppMinioProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppMinioAutoConfiguration {

    @Autowired
    private AppMinioProperties appMinioProperties;

    public AppMinioAutoConfiguration() {
        log.info("自动装配 App Minio 自动化配置");
    }

    @Bean("minioClient")
    @ConditionalOnMissingBean
    public MinioClient getMinioClient() {
        return MinioClient.builder()
                .endpoint(appMinioProperties.getEndpoint())
                .credentials(appMinioProperties.getAccessKey(), appMinioProperties.getSecretKey())
                .build();
    }

    @Bean("appMinioService")
    @DependsOn("minioClient")
    @ConditionalOnBean(MinioClient.class)
    @ConditionalOnMissingBean
    public AppMinioService getSysConfigService() {
        return new AppMinioService();
    }

    @Bean("appMinioRest")
    @DependsOn("appMinioService")
    @ConditionalOnBean(AppMinioService.class)
    @ConditionalOnMissingBean
    public AppMinioRest getAppMinioRest() {
        return new AppMinioRest();
    }
}
