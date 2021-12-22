package com.toquery.framework.example.bff.admin.news.category.model.response;

import com.alibaba.excel.annotation.ExcelProperty;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BizNewsCategoryInfoResponse extends AppBaseEntity {

    private String categoryName;

}
