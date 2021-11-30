package com.toquery.framework.example.bff.admin.news.model.response;

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
public class BizNewsTreeResponse extends AppBaseEntity {

    private String title;

    private Long showNum;

    private Integer likeNum;

    private BizNewsShowStatus showStatus;

    private Collection<BizType> types = new HashSet<>();

    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private Date showTime;

    @JsonFormat(pattern = AppCommonConstant.DATE_PATTERN)
    private LocalDate localDate;

    @JsonFormat(pattern = AppCommonConstant.TIME_PATTERN)
    private LocalTime localTime;

    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime localDateTime;

}
