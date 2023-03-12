package io.github.toquery.framework.security.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = AppSecurityJWTProperties.PREFIX)
public class AppSecurityJWTProperties {

    public static final String PREFIX = "app.security.jwt";


    private String issuer = "clever-framework";

    /**
     * 过期时间
     */
    private Duration expires = Duration.ofHours(24L);

    /**
     * JWT令牌自动续约提前时间(比如在过期前提前5分钟续约)
     */
    private Duration renewalAheadTime = Duration.ofHours(1L);


    /**
     * 是否忽略Token已过期?
     * <p>如果忽略，不会启用自动刷新Token功能，修改密码后也不会影响Token</p>
     * todo <p>如果不忽略，会自动刷新Token，修改密码后需要重新登录</p>
     */
    private boolean ignoreTokenExpires = false;

}
