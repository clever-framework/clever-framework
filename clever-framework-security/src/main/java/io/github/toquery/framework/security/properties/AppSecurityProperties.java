package io.github.toquery.framework.security.properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.security.constant.TokenTypeConstant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Data
@Component
@ConfigurationProperties(prefix = AppSecurityProperties.PREFIX)
public class AppSecurityProperties {

    public static final String PREFIX = "app.security";

    private boolean enabled = true;

    /**
     * 是否启用 defaultAuthenticationManager
     */
    private boolean enabledDAF = true;

    /**
     * 是否启用 jwtEncoder
     */
    private boolean enabledDJE = true;

    /**
     * 是否启用 jwtDecoder
     */
    private boolean enabledDJD = true;

    /**
     * 是否启用 jwtTokenProvider
     */
    private boolean enabledDJP = true;


    /**
     * 是否启用 defaultSecurityFilterChain
     */
    private boolean enabledDSFC = true;

    /**
     * 是否开启用户注册，默认不开启
     */
    private boolean register = false;

    /**
     *
     */
    private TokenTypeConstant tokenType = TokenTypeConstant.JWT;
    /**
     * 配置白名单
     */
    private Set<String> ignoring = Sets.newHashSet();

    //url权限配置
    private List<UrlAuthConfig> urlAuths = Lists.newArrayList();

    private AppSecurityJwtKey key = new AppSecurityJwtKey();

    @Getter
    @Setter
    public static class AppSecurityJwtKey {

        private String keyId = "12356";

        private String issuer = "clever-framework";
        private String publicKey;

        private String privateKey;

    }

    public String[] getIgnoringArray() {
        String[] whitelistArray = new String[ignoring.size()];
        return ignoring.toArray(whitelistArray);
    }

    public Set<String> getIgnoring() {
        return ignoring;
    }

    @Data
    public static final class UrlAuthConfig {

        private String url;

        private String filter;
    }
}
