package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.log.annotation.AppLogEntity;
import io.github.toquery.framework.core.log.annotation.AppLogField;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
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
@Table(name = "sys_dict")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE SysDict SET deleted = true WHERE id = ?")
public class SysDict extends AppBaseEntity implements AppEntitySort, AppEntityLogicDel {

    /**
     * [预留字段，暂时无用]
     * 字典类型,0 string,1 number类型,2 boolean
     * 前端js对stirng类型和number类型 boolean 类型敏感，需要区分。在select 标签匹配的时候会用到
     * 默认为string类型
     */
    @Column(name = "dict_type")
    private Integer dictType;

    /**
     * 字典名称
     */
    @AppLogField("字典名称")
    @Column(name = "dict_name", length = 50)
    private String dictName;

    /**
     * 字典编码
     */
    @AppLogField("字典编码")
    @Column(name = "dict_code", unique = true, length = 50)
    private String dictCode;

    /**
     * 描述
     */
    @AppLogField("描述")
    @Column(name = "description", length = 50)
    private String description;


    @AppLogField("配置排序")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    @Transient
    private List<SysDictItem> dictItems;

    @Override
    public boolean getDeleted() {
        return deleted;
    }
}
