package io.github.toquery.framework.log.properties;

import com.google.common.collect.Sets;
import io.github.toquery.framework.log.constant.AppLogEnableModel;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Data
@Component
@ConfigurationProperties(prefix = AppLogProperties.PREFIX)
public class AppLogProperties {

    public static final String PREFIX = "app.log";

    private boolean enable = true;

    private AppLogEnableModel enableModel = AppLogEnableModel.BASE;

    // 记录日志时忽略的字段，全局配置
    private Set<String> ignoreFields = Sets.newHashSet("log");

}
