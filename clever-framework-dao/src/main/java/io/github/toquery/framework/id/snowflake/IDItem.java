package io.github.toquery.framework.id.snowflake;

public enum IDItem{
    timestamp("时间戳") , business("业务线或模块") , machineroom("机房") ,
    machine("每个机房中机器") , custom("自定义扩展") , num("每毫秒随机业务数字") ;

    public String remark ;

    IDItem( String remark ){
        this.remark = remark ;
    }
}