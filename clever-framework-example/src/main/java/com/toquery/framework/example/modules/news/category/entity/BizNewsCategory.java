package com.toquery.framework.example.modules.news.category.entity;

import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
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
 * @author toquery
 * @version 1
 */
@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@AppLogEntity

@Where(clause = "deleted = false")

@Entity
@Table(name = "biz_news_category")
public class BizNewsCategory extends AppBaseEntity implements AppEntityLogicDel {


    @AppLogField("类型名称")
    @Column(name = "category_name")
    private String categoryName;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @Override
    public Boolean getDeleted() {
        return deleted;
    }


    /*
    @Override
    public void domainEvents() {
        super.domainEvents();
    }

    @Override
    public void afterDomainEventPublication() {
        super.afterDomainEventPublication();
    }
    */
}
