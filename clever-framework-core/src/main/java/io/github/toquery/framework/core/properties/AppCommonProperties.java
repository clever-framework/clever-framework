package io.github.toquery.framework.core.properties;

import io.github.toquery.framework.common.constant.AppCommonConstant;
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

        private String date = AppCommonConstant.DATE_PATTERN;

        private String dateTime = AppCommonConstant.DATE_TIME_PATTERN;

    }
}
