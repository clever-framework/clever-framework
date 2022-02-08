package io.github.toquery.framework.core.properties;

import io.github.toquery.framework.core.constant.AppEnumRoleModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 常量
 *
 * @author toquery
 * @version 1
 */
@Setter
@Getter
//@Configuration
@ConfigurationProperties(prefix = AppProperties.PREFIX)
public class AppProperties {

    public static final String PREFIX = "app";

    private boolean enabled = true;

    /**
     * 后台修改 admin root （用户，角色） 等关键数据的效验
     */
    private String rootPassword = "1qaz2wsx";

    /**
     * 使用应用角色模式，COMPLEX 返回单个并聚合，ISOLATE 独立返回多个角色
     */
    private AppEnumRoleModel roleModel = AppEnumRoleModel.COMPLEX;


//    private AppCommonProperties common = new AppCommonProperties();

    // private AppJpaProperties jpa = new AppJpaProperties();
}
