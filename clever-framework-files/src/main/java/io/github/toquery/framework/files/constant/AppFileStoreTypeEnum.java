package io.github.toquery.framework.files.constant;

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
public enum AppFileStoreTypeEnum {

    DATABASE("数据库"), FILE("保存文件");

    private String remark;
}
