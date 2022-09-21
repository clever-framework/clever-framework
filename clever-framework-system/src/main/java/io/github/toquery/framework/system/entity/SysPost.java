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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *
 */
@Entity
@Getter
@Setter
@TableName(value = "sys_post")
@Table(name = "sys_post")
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_post SET deleted = true WHERE id = ?")
public class SysPost extends AppBaseEntity implements AppEntityTree<SysPost>, AppEntitySort, AppEntityLogicDel {

    public SysPost(Long id, String postName) {
        this.id = id;
        this.postName = postName;
    }

    @NotNull
    @Size(max = 50)
    @TableField(value = "post_name")
    @Column(name = "post_name", length = 50)
    private String postName;

    @NotNull
    @ColumnDefault("1")
    @TableField(value = "post_status")
    @Column(name = "post_status")
    private Integer postStatus = 1;

    // 树状结构
    @TableField(value = "post_level")
    @Column(name = "post_level")
    private int postLevel = 0;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "parent_id")
    @Column(name = "parent_id")
    private Long parentId;

    @TableField(value = "")
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
    private SysPost parent;

    /**
     * 子集
     */
    @TableField(exist = false)
    @Transient
    private List<SysPost> children;


    public boolean isHasChildren() {
        return hasChildren;
    }

    public boolean getHasChildren() {
        return hasChildren;
    }

    @Override
    public int getLevel() {
        return this.postLevel;
    }

    @Override
    public void setLevel(int postLevel) {
        this.postLevel = postLevel;
    }

    @Override
    public int compareTo(SysPost sysPost) {
        return sysPost.getSortNum();
    }

}
