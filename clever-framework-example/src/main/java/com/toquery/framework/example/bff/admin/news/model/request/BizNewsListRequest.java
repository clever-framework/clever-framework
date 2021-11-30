package com.toquery.framework.example.bff.admin.news.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.toquery.framework.example.modules.news.constant.BizNewsShowStatus;
import com.toquery.framework.example.modules.news.entity.BizType;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Date;
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
public class BizNewsListRequest extends AppBaseEntity {

    private String title;

    private Long showNum;

    private Integer likeNum;

    private BizNewsShowStatus showStatus;

    private Collection<BizType> types = new HashSet<>();

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private Date showTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonFormat(pattern = AppCommonConstant.DATE_PATTERN)
    private LocalDate localDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    @JsonFormat(pattern = AppCommonConstant.TIME_PATTERN)
    private LocalTime localTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime localDateTime;
}
