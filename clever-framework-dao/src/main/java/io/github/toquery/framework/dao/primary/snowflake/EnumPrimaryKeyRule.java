package io.github.toquery.framework.dao.primary.snowflake;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 雪花主键生成规则
 */
@Getter
@AllArgsConstructor
public enum EnumPrimaryKeyRule {
    timestamp("时间戳"), business("业务线或模块"), machine_room("机房"),
    machine("每个机房中机器"), custom("自定义扩展"), num("每毫秒随机业务数字");

    public String remark;
}