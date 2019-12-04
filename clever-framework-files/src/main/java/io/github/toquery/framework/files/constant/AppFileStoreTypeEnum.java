package io.github.toquery.framework.files.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author toquery
 * @version 1
 * 2019-12-04 DATABASE 通过数据库的方式读存文件，但不携带文件后缀，导致浏览器识别错误。替代为db返回文件格式
 *
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AppFileStoreTypeEnum {

    DATABASE("数据库,废弃"),
    DB("数据库2"),
    FILE("保存文件");

    private String remark;
}
