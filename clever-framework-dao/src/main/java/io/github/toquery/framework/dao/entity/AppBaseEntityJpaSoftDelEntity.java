package io.github.toquery.framework.dao.entity;

/**
 * 是否是假删除
 */
public interface AppBaseEntityJpaSoftDelEntity {

    Boolean getDel() ;

    void setDel(Boolean del) ;

}
