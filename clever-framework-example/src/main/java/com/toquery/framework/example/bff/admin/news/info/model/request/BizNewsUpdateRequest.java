package com.toquery.framework.example.bff.admin.news.info.model.request;

import com.toquery.framework.example.modules.news.info.constant.BizNewsShowStatus;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

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
public class BizNewsUpdateRequest extends AppBaseEntity {

    private String title;

    private Long categoryId;

    private Long showNum;

    private Integer likeNum;

    private BizNewsShowStatus showStatus;

    private Date showTime;

    private LocalDate localDate;

    private LocalTime localTime;

    private LocalDateTime localDateTime;

}
