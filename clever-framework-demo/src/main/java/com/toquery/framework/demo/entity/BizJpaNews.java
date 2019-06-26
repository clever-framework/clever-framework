package com.toquery.framework.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "biz_jpa_news")
public class BizJpaNews extends AppBaseEntity {

    public BizJpaNews(String name, Date loginTime) {
        this.name = name;
        this.showTime = loginTime;
    }

    @Column
    private String name;

    @Column
    private Integer num;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "show_time")
    private Date showTime;
}
