package io.github.toquery.framework.security.jwt.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Data
@Configuration
@ConfigurationProperties(prefix = AppJwtProperties.PATH)
public class AppJwtProperties {

    public static final String PATH = "app.jwt";

    private String header = "Authorization";

    private AppJwtParamProperties param = new AppJwtParamProperties();

    private String secret = "";

    private Long expiration = 3600L;

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
        private String userName = "username";
        private String password = "password";
    }

}
