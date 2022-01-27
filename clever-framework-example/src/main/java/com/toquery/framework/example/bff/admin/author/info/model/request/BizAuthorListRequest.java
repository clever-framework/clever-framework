package com.toquery.framework.example.bff.admin.author.info.model.request;

import com.toquery.framework.example.bff.admin.author.info.model.constant.QueryType;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author toquery
 * @version 1
 */

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BizAuthorListRequest extends AppBaseEntity {

    @UpperCase
    private QueryType queryType;

    private String authorName;
}
