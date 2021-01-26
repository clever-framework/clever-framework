package io.github.toquery.framework.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntitySoftDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Entity
@Getter
@Setter
@AppLogEntity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_dict_item")
public class SysDictItem extends AppBaseEntity implements AppEntitySort, AppEntitySoftDel {


    /**
     * 字典id
     */
    @Column(name = "dict_id")
    private Long dictId;

    /**
     * 字典项文本
     */
    @Column(name = "item_text")
    private String itemText;

    /**
     * 字典项值
     */
    @Column(name = "item_value")
    private String itemValue;

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 状态（1启用 0不启用）
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 排序
     */
    @AppLogField("配置排序")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

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
