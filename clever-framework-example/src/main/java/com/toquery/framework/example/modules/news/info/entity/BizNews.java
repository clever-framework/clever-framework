package com.toquery.framework.example.modules.news.info.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.toquery.framework.example.modules.news.info.constant.BizNewsShowStatus;
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
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

//@AppEntityRest(path = "example-biz-news-rest")

@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE biz_news SET deleted = true WHERE id = ?")

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
//@DynamicInsert
//@DynamicUpdate
@Table(name = "biz_news")
@TableName("biz_news")
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
    @TableField
    private String title;

    @TableField("category_id")
    @Column(name = "category_id")
    private Long categoryId;

    @AppLogField("查看次数")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField("show_num")
    @Column(name = "show_num")
    private Long showNum;

    @AppLogField("点赞次数")
    @TableField("like_num")
    @Column(name = "like_num")
    private Integer likeNum;

    @AppLogField("展示状态")
    @TableField("show_status")
    @Enumerated(EnumType.STRING)
    @Column(name = "show_status")
    private BizNewsShowStatus showStatus;

    @AppLogField("显示时间")
    @TableField("show_time")
    @Column(name = "show_time")
    private Date showTime;

    @TableField("local_time")
    @Column(name = "local_time")
    private LocalTime localTime;

    @TableField("local_date")
    @Column(name = "local_date")
    private LocalDate localDate;

    @TableField("local_date_time")
    @Column(name = "local_date_time")
    private LocalDateTime localDateTime;

    @TableField
    private String content;

    /**
     * 是否删除：1已删除；0未删除
     */
    @TableField("deleted")
    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;


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
