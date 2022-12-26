package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.domain.AppEntityTree;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@TableName(value = "sys_dept")
@Table(name = "sys_dept")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_dept SET deleted = true WHERE id = ?")
public class SysDept extends AppBaseEntity implements AppEntityTree<SysDept>, AppEntitySort, AppEntityLogicDel {

    public SysDept(Long id, String deptName) {
        this.id = id;
        this.deptName = deptName;
    }


    public SysDept(Long id, String deptName, Long parentId, String parentIds) {
        this.id = id;
        this.deptName = deptName;
        this.parentId = parentId;
        this.parentIds = parentIds;
    }

    @NotNull
    @Size(max = 50)
    @TableField(value = "dept_name")
    @Column(name = "dept_name", length = 50)
    private String deptName;

    @NotNull
    @ColumnDefault("1")
    @TableField(value = "dept_status")
    @Column(name = "dept_status")
    private Integer deptStatus = 1;

    // 树状结构
    @TableField(value = "dept_level")
    @Column(name = "dept_level")
    private int deptLevel = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "parent_id")
    @Column(name = "parent_id")
    private Long parentId;

    @TableField(value = "parent_ids")
    @Column(name = "parent_ids")
    private String parentIds;

    @TableField(value = "tree_path")
    @Column(name = "tree_path")
    private String treePath;

    @TableField(value = "has_children")
    @Column(name = "has_children")
    private boolean hasChildren = false;

    @TableField(value = "sort_num")
    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @TableLogic
    @ColumnDefault("false")
    @TableField(value = "deleted")
    @Column(name = "deleted")
    private Boolean deleted = false;

    /**
     * 父级信息
     */
    @TableField(exist = false)
    @Transient
    private SysDept parent;

    /**
     * 子集
     */
    @TableField(exist = false)
    @Transient
    private List<SysDept> children;

    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean getHasChildren() {
        return hasChildren;
    }

    @Override
    public int getLevel() {
        return this.deptLevel;
    }

    @Override
    public void setLevel(int deptLevel) {
        this.deptLevel = deptLevel;
    }

    @Override
    public int compareTo(SysDept sysDept) {
        return sysDept.getSortNum();
    }

}
