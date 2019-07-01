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
public enum AppLogEnableModel {

    NONE("不使用"), BASE("使用创建/修改/删除"), QUERY("查询"), ALL("全部");

    private String remark;
}
