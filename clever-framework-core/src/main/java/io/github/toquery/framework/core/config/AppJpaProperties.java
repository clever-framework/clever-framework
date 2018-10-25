package io.github.toquery.framework.core.config;

import lombok.Data;

/**
 * @author toquery
 * @version 1
 */
@Data
public class AppJpaProperties {

    private AppJpaColumnProperties column = new AppJpaColumnProperties();

    @Data
    public static class AppJpaColumnProperties {
        private String createBy;
    }
}
