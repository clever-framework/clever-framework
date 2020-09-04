package com.toquery.framework.example.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toquery.framework.example.constant.BizNewsShowStatus;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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
@Table(name = "biz_news")
public class BizNews extends AppBaseEntity  {


    public BizNews(String title, Date showTime) {
        this.title = title;
        this.showTime = showTime;
    }

    public BizNews(Long id, String title, Date showTime) {
        this.id = id;
        this.title = title;
        this.showTime = showTime;
    }

    @AppLogField("标题")
    @Column
    private String title;

    @AppLogField("查看次数")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "show_num")
    private Long showNum;

    @AppLogField("点赞次数")
    @Column(name = "like_num")
    private Integer likeNum;


    @AppLogField("展示状态")
    @Enumerated(EnumType.STRING)
    @Column(name = "show_status")
    private BizNewsShowStatus showStatus;

    @AppLogField("显示时间")
    @JsonFormat
    // @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, timezone = "GMT+8")
    @Column(name = "show_time")
    private Date showTime;


//    @AppLogField("instant")
//    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @Column(name = "instant")
//    private Instant instant;


//    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
//    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @Column(name = "local_time")
//    private LocalTime localTime;


//    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
//    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @Column(name = "local_date")
//    private LocalDate localDate;


//    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)

    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;


//    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @Column(name = "offset_time")
//    private OffsetTime offsetTime;

//    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    @Column(name = "offset_date_time")
    private OffsetDateTime offsetDateTime;

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
