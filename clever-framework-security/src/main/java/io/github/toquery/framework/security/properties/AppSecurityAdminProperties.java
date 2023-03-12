package io.github.toquery.framework.security.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = AppSecurityAdminProperties.PREFIX)
public class AppSecurityAdminProperties {

    public static final String PREFIX = "app.security.admin";

    /**
     * 是否开启用户注册，默认不开启
     */
    private boolean register = false;

    private AppSecurityAdminParamProperties param = new AppSecurityAdminParamProperties();

    private AppSecurityAdminPathProperties path = new AppSecurityAdminPathProperties();


    @Getter
    @Setter
    public static class AppSecurityAdminPathProperties {
        private String token = "/admin/login";
        private String info = "/admin/info";
        private String refresh = "/admin/refresh";
        private String register = "/admin/register";
        private String logout = "/admin/logout";
    }

    @Getter
    @Setter
    public static class AppSecurityAdminParamProperties {
        private String username = "username";
        private String password = "password";
    }

}
