package com.toquery.framework.example.bff.admin.news.info.model.request;

import com.alibaba.excel.annotation.ExcelProperty;
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
public class BizNewsImportRequest extends AppBaseEntity {

    @ExcelProperty("标题")
    private String title;
    private Long categoryId;

    @ExcelProperty("序号")
    private Long showNum;

    @ExcelProperty("点赞数量")
    private Integer likeNum;

    @ExcelProperty("显示状态")
    private BizNewsShowStatus showStatus;

    @ExcelProperty("显示时间")
    private Date showTime;

    @ExcelProperty("日期")
    private LocalDate localDate;

    @ExcelProperty("时间")
    private LocalTime localTime;

    @ExcelProperty("日期时间")
    private LocalDateTime localDateTime;

}