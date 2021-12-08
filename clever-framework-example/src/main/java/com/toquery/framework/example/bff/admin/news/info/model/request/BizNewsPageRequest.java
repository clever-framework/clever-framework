package com.toquery.framework.example.bff.admin.news.info.model.request;

import com.toquery.framework.example.bff.admin.news.info.model.constant.QueryType;
import com.toquery.framework.example.modules.news.info.constant.BizNewsShowStatus;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BizNewsPageRequest extends AppBaseEntity {

    private Integer current;

    private Integer pageSize;

    @UpperCase
    private QueryType queryType;

    private String title;

    private Long categoryId;

    private Long showNum;

    private Integer likeNum;

    private BizNewsShowStatus showStatus;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date showTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate localDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime localTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime localDateTime;
}
