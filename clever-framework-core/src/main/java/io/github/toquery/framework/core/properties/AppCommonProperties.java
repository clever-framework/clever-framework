package io.github.toquery.framework.core.properties;

import io.github.toquery.framework.core.constant.AppPropertiesDefault;
import lombok.Getter;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
public class AppCommonProperties {

    private AppCommonPatternProperties pattern = new AppCommonPatternProperties();

    @Setter
    @Getter
    public static class AppCommonPatternProperties {

        private String date = AppPropertiesDefault.DATE_PATTERN;

        private String dateTime = AppPropertiesDefault.DATE_TIME_PATTERN;

    }
}
