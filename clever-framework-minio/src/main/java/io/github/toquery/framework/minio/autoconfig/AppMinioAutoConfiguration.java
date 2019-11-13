package io.github.toquery.framework.minio.autoconfig;

import io.github.toquery.framework.minio.properties.AppMinioProperties;
import io.github.toquery.framework.minio.service.MinioService;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AppMinioProperties.class)
@ConditionalOnProperty(prefix = AppMinioProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppMinioAutoConfiguration {

    @Autowired
    private AppMinioProperties appMinioProperties;

    public AppMinioAutoConfiguration() {
        log.info("开始自动装配 App Minio 自动化配置");
    }

    @Bean
    @ConditionalOnMissingBean
    public MinioClient getMinioClient() throws InvalidPortException, InvalidEndpointException {
        return new MinioClient(appMinioProperties.getEndpoint(), appMinioProperties.getAccessKey(), appMinioProperties.getSecretKey());
    }

    @Bean
    @ConditionalOnBean(MinioClient.class)
    @ConditionalOnMissingBean
    public MinioService getSysConfigService() throws InvalidPortException, InvalidEndpointException {
        return new MinioService(getMinioClient());
    }
}
