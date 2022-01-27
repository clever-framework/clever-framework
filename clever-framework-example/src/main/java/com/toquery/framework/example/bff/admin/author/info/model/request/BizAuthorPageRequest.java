package com.toquery.framework.example.bff.admin.author.info.model.request;

import com.toquery.framework.example.bff.admin.author.info.model.constant.QueryType;
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
public class BizAuthorPageRequest extends AppBaseEntity {

    private Integer current = 1;

    private Integer pageSize = 10;

    @UpperCase
    private QueryType queryType;

    private String authorName;
}
