package com.toquery.framework.example.bff.admin.news.category.model.response;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class BizNewsCategoryPageResponse extends AppBaseEntity {

    @Schema(description = "新闻类目名称", example = "科技", required = true)
    private String categoryName;

}
