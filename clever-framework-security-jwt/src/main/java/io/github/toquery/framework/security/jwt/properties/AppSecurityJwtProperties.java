package io.github.toquery.framework.security.jwt.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppSecurityJwtProperties.PATH)
public class AppSecurityJwtProperties {

    public static final String PATH = "app.jwt";

    private String header = "Authorization";

    private String secret = "clever";

    // 秒
    private Long expiration = 3600L;

    private AppJwtParamProperties param = new AppJwtParamProperties();

    private AppJwtPathProperties path = new AppJwtPathProperties();

    @Getter
    @Setter
    public static class AppJwtPathProperties {
        private String token = "/user/token";
        private String info = "/user/info";
        private String refresh = "/user/refresh";
        private String register = "/user/register";
    }

    @Getter
    @Setter
    public static class AppJwtParamProperties {
        private String username = "username";
        private String password = "password";
    }

}
