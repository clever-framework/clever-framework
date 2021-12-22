package com.toquery.framework.example.bff.admin.news.category.model.request;

import io.github.toquery.framework.core.log.annotation.AppLogField;
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
public class BizNewsCategoryUpdateRequest extends AppBaseEntity {

    @AppLogField("类型名称")
    private String categoryName;

}
