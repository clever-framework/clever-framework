package io.github.toquery.framework.dao.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;

/**
 * 是否是假删除 逻辑
 */
public interface AppEntityLogicDel {

    Boolean getDeleted() ;

    void setDeleted(Boolean deleted) ;

}
