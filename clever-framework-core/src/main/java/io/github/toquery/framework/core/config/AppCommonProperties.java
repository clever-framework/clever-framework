package io.github.toquery.framework.core.config;

import io.github.toquery.framework.core.constant.AppPropertiesDefault;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "app.common")
public class AppCommonProperties {

    private Pattern pattern = new Pattern();

    @Setter
    @Getter
    public static class Pattern {

        private String date = AppPropertiesDefault.DATE_PATTERN;

        private String dateTime = AppPropertiesDefault.DATE_TIME_PATTERN;

    }
}
