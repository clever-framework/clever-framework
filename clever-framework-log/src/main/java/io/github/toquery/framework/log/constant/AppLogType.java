package io.github.toquery.framework.log.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author toquery
 * @version 1
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AppLogType {

    CREA("创建"), MODF("修改"), DEL("删除"), SEARCH("查看");

    private String remark;
}
