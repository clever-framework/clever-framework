package io.github.toquery.framework.id.snowflake;

import java.util.Map;

public interface SnowflakeIDPart {

    String getName() ;

    /**
     * 设置id生成器中每部分id的长度
     * @param idItems
     */
    void setIdItems(Map<IDItem, Integer> idItems) ;

    /**
     * 获取id的组成
     * @return
     */
    Map<IDItem,Integer> getIdItems() ;

    /**
     * 按照毫秒生成唯一的随机数
     * @param binaryLength
     * @return
     */
    long getNumIndex(int binaryLength, long timeSeconds, Object object) ;
    /**
     * 获取自定义字段内容索引
     * @param binaryLength
     * @return
     */
    long getCustomIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取机器的标识，默认收集第三部分，<b>在docker中宿主机的ip默认是第三部分</b>
     * @return
     */
    long getMachineIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取机房索引
     * @return
     */
    long getMachineRoomIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取业务线索引
     * @return
     */
    long getBusinessIndex(int binaryLength, long timeSeconds, Object object) ;

    /**
     * 获取时间索引
     * @return
     */
    long getTimeIndex(Integer binaryLength, long timeSeconds, Object object) ;

}
