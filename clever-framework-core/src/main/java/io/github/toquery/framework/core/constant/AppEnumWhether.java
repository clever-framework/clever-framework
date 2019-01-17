package io.github.toquery.framework.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author toquery
 * @version 1
 */
@Getter
@AllArgsConstructor
public enum AppEnumWhether {
    YES("1"), NO("0");
    private String value;
}
