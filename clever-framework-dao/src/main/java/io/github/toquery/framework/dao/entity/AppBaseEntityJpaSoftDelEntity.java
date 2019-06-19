package io.github.toquery.framework.dao.entity;

/**
 * 是否是假删除
 */
public interface AppBaseEntityJpaSoftDelEntity {

    boolean getDel() ;

    void setDel(boolean del) ;

}
