package com.toquery.framework.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.log.annotation.AppLogEntity;
import io.github.toquery.framework.log.annotation.AppLogField;
import io.github.toquery.framework.log.annotation.AppLogIgnoreField;
import io.github.toquery.framework.security.AppEntityAuditorListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Setter
@Getter
@Entity
@AppLogEntity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "biz_jpa_news")
public class BizJpaNews extends AppBaseEntity implements AppEntityAuditorListener {

    public BizJpaNews(String name, Date loginTime) {
        this.name = name;
        this.showTime = loginTime;
    }

    @AppLogField("名称")
    @Column
    private String name;

    @AppLogIgnoreField
    @Column
    private Integer num;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "show_time")
    private Date showTime;

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
