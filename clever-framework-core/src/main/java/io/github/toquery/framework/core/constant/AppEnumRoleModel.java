package io.github.toquery.framework.core.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 应用角色模式
 */
@Getter
@AllArgsConstructor
public enum AppEnumRoleModel {

    COMPLEX("聚合模式，统一返回所有角色"), ISOLATE("隔离模式，单独角色");
    private final String value;
}
