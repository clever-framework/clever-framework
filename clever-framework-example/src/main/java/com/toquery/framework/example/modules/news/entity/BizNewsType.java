package com.toquery.framework.example.modules.news.entity;

import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 */
@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@AppLogEntity

@Where(clause = "deleted = false")

@Entity
@Table(name = "biz_news_type")
public class BizNewsType extends AppBaseEntity implements AppEntityLogicDel {

    @Column(name = "type_id")
    private Long typeId;

    @Column(name = "news_id")
    private Long newsId;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    @Override
    public boolean getDeleted() {
        return deleted;
    }
}
