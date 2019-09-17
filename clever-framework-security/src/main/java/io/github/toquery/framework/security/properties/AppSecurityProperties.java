package io.github.toquery.framework.security.properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Data;
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

    private boolean enable = true;

    /**
     * 后台修改 admin root （用户，角色） 等关键数据的效验
     */
    private String rootPwd = "1qaz2wsx";

    /**
     * 配置白名单
     */
    private Set<String> whitelist = Sets.newHashSet();

    //url权限配置
    private List<UrlAuthConfig> urlAuths = Lists.newArrayList();

    public String[] getWhitelistArray() {
        String[] whitelistArray = new String[whitelist.size()];
        return whitelist.toArray(whitelistArray);
    }

    public Set<String> getWhitelist() {
        return whitelist;
    }

    @Data
    public class UrlAuthConfig {

        private String url;

        private String filter;
    }
}
