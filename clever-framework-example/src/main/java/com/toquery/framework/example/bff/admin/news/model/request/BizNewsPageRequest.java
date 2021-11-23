package com.toquery.framework.example.bff.admin.news.model.request;

import com.toquery.framework.example.constant.BizNewsShowStatus;
import com.toquery.framework.example.modules.news.entity.BizType;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
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
public class BizNewsPageRequest extends AppBaseEntity{

    private String title;

    private Long showNum;

    private Integer likeNum;

    private BizNewsShowStatus showStatus;

    private Collection<BizType> types = new HashSet<>();

    private Date showTime;

    private LocalDateTime localDateTime;
    /**
     * 是否删除：1已删除；0未删除
     */
    private boolean deleted = false;

    public boolean getDeleted() {
        return deleted;
    }
}
