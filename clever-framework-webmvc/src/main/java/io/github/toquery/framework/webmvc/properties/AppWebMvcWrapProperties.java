package io.github.toquery.framework.webmvc.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 *
 */
@Setter
@Getter
@Slf4j
public class AppWebMvcWrapProperties {

    /**
     * 是否自动包裹响应结果
     */
    private boolean enabled = false;

    /**
     * 是否包裹 void 类型
     */
    private boolean voidObject = false;

}
