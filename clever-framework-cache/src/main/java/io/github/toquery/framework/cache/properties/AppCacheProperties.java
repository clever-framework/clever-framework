package io.github.toquery.framework.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@Data
@ConfigurationProperties(prefix = AppCacheProperties.PREFIX_CACHE)
public class AppCacheProperties {

    public static final String PREFIX_CACHE = "app.cache";

    /**
     * 是否开启缓存
     */
    private boolean enabled = true;

    /**
     * 模式(默认local)
     */
    private CacheMode mode = CacheMode.local;

    public enum CacheMode {
        local,
        memory;

        CacheMode() {
        }
    }
}
