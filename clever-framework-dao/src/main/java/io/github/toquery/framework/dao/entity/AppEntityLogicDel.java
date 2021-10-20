package io.github.toquery.framework.dao.entity;

/**
 * 是否是假删除 逻辑
 */
public interface AppEntityLogicDel {

    boolean getDeleted() ;

    void setDeleted(boolean deleted) ;

}
