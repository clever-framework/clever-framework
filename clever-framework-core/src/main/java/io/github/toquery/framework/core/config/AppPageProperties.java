package io.github.toquery.framework.core.config;

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
@ConfigurationProperties(prefix = "app.page")
public class AppPageProperties {


    private AppPageParamProperties param = new AppPageParamProperties();

    @Setter
    @Getter
    public class AppPageParamProperties {
        private String pageSize;
        private String pageNumber;
        private String totalElements;
        private String totalPages;
    }
}
