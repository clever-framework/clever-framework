package io.github.toquery.framework.dao.primary.snowflake;

import java.util.Map;


public interface SnowflakePrimaryKey {

    String getName() ;

    /**
     * 设置id生成器中每部分id的长度
     */
    void setIdItems(Map<EnumPrimaryKeyRule, Integer> idItems) ;

    /**
     * 获取id的组成
     */
    Map<EnumPrimaryKeyRule,Integer> getIdItems() ;

    /**
     * 按照毫秒生成唯一的随机数
     */
    long getNumIndex(int binaryLength, long timeSeconds, Object object) ;
    /**
     * 获取自定义字段内容索引
     */
    long getCustomIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取机器的标识，默认收集第三部分，<b>在docker中宿主机的ip默认是第三部分</b>
     */
    long getMachineIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取机房索引
     */
    long getMachineRoomIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取业务线索引
     */
    long getBusinessIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取时间索引
     */
    long getTimeIndex(Integer binaryLength, long timeSeconds, Object object) ;

}
