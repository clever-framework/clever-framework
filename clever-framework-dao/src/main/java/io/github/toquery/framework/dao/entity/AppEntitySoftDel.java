package io.github.toquery.framework.dao.entity;

/**
 * 是否是假删除
 */
public interface AppEntitySoftDel {

    boolean getDeleted() ;

    void setDeleted(boolean deleted) ;

}
