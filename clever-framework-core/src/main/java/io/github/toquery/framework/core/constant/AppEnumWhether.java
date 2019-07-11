package io.github.toquery.framework.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author toquery
 * @version 1
 * @deprecated 1.0.5 并不常用
 */
@Getter
@Deprecated
@AllArgsConstructor
public enum AppEnumWhether {
    YES("1"), NO("0");
    private String value;
}
