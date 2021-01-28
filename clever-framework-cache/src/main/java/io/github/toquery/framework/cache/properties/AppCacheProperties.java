package io.github.toquery.framework.cache.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 *
 */
@Data
@ConfigurationProperties(prefix = AppCacheProperties.PREFIX)
public class AppCacheProperties {

    public static final String PREFIX = "app.cache";

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
