package io.github.toquery.framework.security.properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@ConfigurationProperties(prefix = AppSecurityProperties.PREFIX)
public class AppSecurityProperties {

    public static final String PREFIX = "app.secure";

    private AuthConfig auth = new AuthConfig();


    @Getter
    @Setter
    public static class AuthConfig {
        /**
         * 本地系统用户
         */
        public static final String LOCAL_SYSTEM = "local";

        private boolean enable = true;

        //登录地址
        private String loginUrl = "/login";

        //登录时使用的用户名参数
        private String loginUsernameParam = "u";

        //登录时使用的密码参数
        private String loginPasswordParam = "p";

        //登录成功的地址
        private String successUrl = "/index";

        //未授权的地址
        private String unauthorizedUrl = "/unauthorized";

        //认证类型，本地系统用户(local) 或 灯塔接口用户(dt)，不同类型使用逗号分隔
        private String type = LOCAL_SYSTEM;

        // 默认的用户角色，在新加用户后会默认添加
        private Set<String> defaultUserRoles = Sets.newHashSet();

        //url权限配置
        private List<UrlAuthConfig> urlAuths = Lists.newArrayList();

    }

    @Getter
    @Setter
    public static class UrlAuthConfig {

        private String url;

        private String filter;
    }
}
