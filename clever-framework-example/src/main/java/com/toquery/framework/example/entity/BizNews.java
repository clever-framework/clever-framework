package com.toquery.framework.example.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.toquery.framework.example.constant.BizNewsShowStatus;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import io.github.toquery.framework.data.rest.annotation.AppEntityRest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * @author toquery
 * @version 1
 */

@Loader(namedQuery = "findBizNewsById") // todo
@NamedQuery(name = "findBizNewsById", query =
        "SELECT news FROM BizNews news WHERE news.id = ?1 AND news.deleted = false")

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@AppLogEntity

@AppEntityRest(path = "example-biz-news-rest")

@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE BizNews SET deleted = true WHERE id = ?")

@FilterDef(
        name = "gtNum",
        parameters = {
                @ParamDef(name = "showNum", type = "int"),
                @ParamDef(name = "likeNum", type = "int")
        }
)
@Filters(value = {
        @Filter(name = "gtNum",
                condition = "show_num > :showNum"
        ),
        @Filter(name = "gtNum",
                condition = "like_num > :likeNum"
        )
})


@Entity
@Table(name = "biz_news")
public class BizNews extends AppBaseEntity implements AppEntityLogicDel {


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
    // @JsonProperty // 序列化 反序列化都会起效
    @JsonAlias({"newsTitle", "masterTitle"}) // 只有在反序列化是起效
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


    @JsonIgnoreProperties(value = {"news", "lastUpdateDateTime", "createDateTime"})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "biz_news_type",
            joinColumns = {@JoinColumn(name = "news_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "type_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Collection<BizType> types = new HashSet<>();

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
//    mybatis 不支持 ！！！ Conversion not supported for type java.time.OffsetDateTime
//    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
//    @Column(name = "offset_date_time")
//    private OffsetDateTime offsetDateTime;

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

    @Transient
    private String moduleName = "example新闻模块";

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
