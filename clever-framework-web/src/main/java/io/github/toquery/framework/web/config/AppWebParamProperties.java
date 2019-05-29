package io.github.toquery.framework.web.config;

import lombok.Getter;
import lombok.Setter;

/**
 * 请求参数key
 *
 * @author toquery
 * @version 1
 */

@Setter
@Getter
public class AppWebParamProperties {
    //    当前页号，从0开始
    private String pageNumber = "pageNumber";
    //    分页大小
    private String pageSize = "pageSize";
    //    过滤参数前缀
    private String filterPrefix = "filter_";
}
