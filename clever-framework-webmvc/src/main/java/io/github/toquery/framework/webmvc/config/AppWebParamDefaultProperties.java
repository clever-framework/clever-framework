package io.github.toquery.framework.webmvc.config;

import lombok.Getter;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
public class AppWebParamDefaultProperties {

    private int pageNumber = 0;
    private int pageSize = 20;
}