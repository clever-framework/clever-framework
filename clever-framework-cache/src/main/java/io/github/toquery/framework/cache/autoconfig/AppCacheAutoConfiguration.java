package io.github.toquery.framework.cache.autoconfig;

import io.github.toquery.framework.cache.properties.AppCacheProperties;
import io.github.toquery.framework.cache.service.ICacheService;
import io.github.toquery.framework.cache.service.impl.MemoryCacheService;
import io.github.toquery.framework.cache.service.impl.RedisCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@EnableConfigurationProperties(AppCacheProperties.class)
public class AppCacheAutoConfiguration {

    public AppCacheAutoConfiguration() {
        log.info("初始化 App Cache 自动配置");
    }

    @Bean
    @ConditionalOnProperty(prefix = AppCacheProperties.PREFIX_CACHE, name = "mode", havingValue = "memory", matchIfMissing = true)
    @ConditionalOnMissingBean(ICacheService.class)
    public ICacheService memoryCache() {
        log.info("使用 App Cache Memory 方式");
        return new MemoryCacheService();
    }


    /**
     * redis缓存
     */
    @Bean
    @DependsOn("redisTemplate")
    @ConditionalOnMissingBean(ICacheService.class)
    @ConditionalOnProperty(prefix = AppCacheProperties.PREFIX_CACHE, name = "mode", havingValue = "redis")
    public ICacheService redisCache(RedisTemplate<String, Object> redisTemplate) {
        log.info("使用 App Cache Redis 方式");
        return new RedisCacheService(redisTemplate);
    }
}
