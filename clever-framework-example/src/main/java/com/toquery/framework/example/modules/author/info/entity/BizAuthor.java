package com.toquery.framework.example.modules.author.info.entity;

import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.Filters;
import org.hibernate.annotations.Loader;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author toquery
 * @version 1
 */

@Loader(namedQuery = "findBizAuthorById")
@NamedQuery(name = "findBizAuthorById", query =
        "SELECT author FROM BizAuthor author WHERE author.id = ?1 AND author.deleted = false")

@Slf4j
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor

@AppLogEntity


@Where(clause = "deleted = false")
@SQLDelete(sql = "UPDATE biz_author SET deleted = true WHERE id = ?")

@FilterDef(
        name = "gtNum",
        parameters = {
                @ParamDef(name = "showNum", type = "int"),
                @ParamDef(name = "likeNum", type = "int")
        }
)
@Filters(value = {
        @Filter(name = "authorNameEq",
                condition = "author_name > :authorName"
        ),
        @Filter(name = "authorNameLike",
                condition = "author_name like '%:likeNum%'"
        )
})
@Entity
@Table(name = "biz_author")
public class BizAuthor extends AppBaseEntity implements AppEntityLogicDel {


    @AppLogField("标题")
    @Column
    private String authorName;


    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    @Override
    public boolean getDeleted() {
        return deleted;
    }
}
