package com.toquery.framework.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
@Table(name = "biz_batis_news")
public class BizBatisNews extends AppBaseEntity {

    @Column
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "show_time")
    private Date showTime;


    public BizBatisNews(Long id, String name) {
        this.id = id;
        this.name = name;
        this.showTime = new Date();
    }
}
