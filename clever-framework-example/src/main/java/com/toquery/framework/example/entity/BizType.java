package com.toquery.framework.example.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.HashSet;

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
@Table(name = "biz_type")
public class BizType extends AppBaseEntity implements AppEntityLogicDel {



    @AppLogField("类型名称")
    @Column(name = "type_name")
    private String typeName;


    @JsonIgnoreProperties(value = {"types", "updateDateTime", "createDateTime"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "biz_news_type",
            joinColumns = {@JoinColumn(name = "type_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "news_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Collection<BizNews> news = new HashSet<>();


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
