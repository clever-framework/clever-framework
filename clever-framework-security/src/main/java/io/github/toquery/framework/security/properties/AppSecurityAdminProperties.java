package io.github.toquery.framework.security.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.security.converter.RsaKeyConverters;

import java.io.File;
import java.io.IOException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = AppSecurityAdminProperties.PREFIX)
public class AppSecurityAdminProperties {

    public static final String PREFIX = "app.security.admin";

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

    private AppSecurityAdminParamProperties param = new AppSecurityAdminParamProperties();

    private AppSecurityAdminPathProperties path = new AppSecurityAdminPathProperties();

    private AppSecurityAdminJwtKey key = new AppSecurityAdminJwtKey();

    @Getter
    @Setter
    public static class AppSecurityAdminJwtKey {
        private RSAPublicKey publicKey;

        private RSAPrivateKey privateKey;

        {
            try {
                publicKey = RsaKeyConverters.x509().convert(new DefaultResourceLoader().getResource(ResourceLoader.CLASSPATH_URL_PREFIX + "jwt" + File.separator + "public.pub").getInputStream());
            } catch (IOException e) {
                log.error("加载JWT公钥失败", e);
                throw new RuntimeException(e);
            }

            try {
                privateKey = RsaKeyConverters.pkcs8().convert(new DefaultResourceLoader().getResource(ResourceLoader.CLASSPATH_URL_PREFIX + "jwt" + File.separator + "private.key").getInputStream());
            } catch (IOException e) {
                log.error("加载JWT私钥失败", e);
                throw new RuntimeException(e);
            }
        }
    }

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
