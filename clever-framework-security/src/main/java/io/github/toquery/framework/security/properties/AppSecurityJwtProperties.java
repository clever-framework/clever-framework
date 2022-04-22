package io.github.toquery.framework.security.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppSecurityJwtProperties.PREFIX)
public class AppSecurityJwtProperties {

    public static final String PREFIX = "app.jwt";

    private String secret = "clever";

    private String issuer = "clever-framework";

    // 秒
    private Long expires = 60 * 60L;

    /**
     * JWT令牌自动续约提前时间(比如在过期前提前5分钟续约)
     */
    private Long renewalAheadTime = 5 * 60L;


    /**
     * 是否忽略Token已过期?
     * <p>如果忽略，不会启用自动刷新Token功能，修改密码后也不会影响Token</p>
     * todo <p>如果不忽略，会自动刷新Token，修改密码后需要重新登录</p>
     */
    private boolean ignoreTokenExpires = false;

    private AppJwtParamProperties param = new AppJwtParamProperties();

    private AppJwtPathProperties path = new AppJwtPathProperties();

    private AppJwtKey key = new AppJwtKey();

    @Getter
    @Setter
    public static class AppJwtKey {
        private RSAPublicKey publicKey;
        private RSAPrivateKey privateKey;
    }

    @Getter
    @Setter
    public static class AppJwtPathProperties {
        private String token = "/user/login";
        private String info = "/user/info";
        private String refresh = "/user/refresh";
        private String register = "/user/register";
        private String logout = "/user/logout";
    }

    @Getter
    @Setter
    public static class AppJwtParamProperties {
        private String username = "username";
        private String password = "password";
    }

}
