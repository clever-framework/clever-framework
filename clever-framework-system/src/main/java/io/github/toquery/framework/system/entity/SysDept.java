package io.github.toquery.framework.system.entity;

import io.github.toquery.framework.core.domain.AppEntitySort;
import io.github.toquery.framework.core.domain.AppEntityTree;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_dept")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE SysDept SET deleted = true WHERE id = ?")
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
    @Column(name = "dept_name", length = 50)
    private String deptName;

    // 树状结构
    @Column(name = "dept_level")
    private int deptLevel = 0;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "parent_ids")
    private String parentIds;

    @Column(name = "tree_path")
    private String treePath;

    @Column(name = "has_children")
    private boolean hasChildren = false;

    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    /**
     * 父级信息
     */
    @Transient
    private SysDept parent;

    /**
     * 子集
     */
    @Transient
    private List<SysDept> children;

    @Override
    public boolean getDeleted() {
        return deleted;
    }

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
