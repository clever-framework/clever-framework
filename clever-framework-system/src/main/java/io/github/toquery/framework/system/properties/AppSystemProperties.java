package io.github.toquery.framework.system.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppSystemProperties.PREFIX)
public class AppSystemProperties {

    public static final String PREFIX = "app.system";

    private boolean enabled = true;

    /**
     * 是否根据代码权限自动写入菜单表
     */
    private boolean generateMenu = true;

    private boolean generateView = true;

    /**
     * 是否允许注册新用户
     */
    private boolean allowRegistration = true;

}
