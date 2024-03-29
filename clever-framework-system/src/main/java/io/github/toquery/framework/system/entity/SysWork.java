package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.core.entity.BaseEntity;
import io.github.toquery.framework.core.entity.EntityLogicDel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import io.github.toquery.framework.common.constant.EntityFieldConstant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

/**
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_work")
@Table(name = "sys_work")
@SQLRestriction(value = EntityFieldConstant.DOMAIN_FIELD_SOFT_DEL + " = false")
@SQLDelete(sql ="UPDATE sys_work SET deleted = true WHERE id = ?")
public class SysWork extends BaseEntity implements EntityLogicDel {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "userId")
    @Column(name = "user_id")
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "dept_id")
    @Column(name = "dept_id")
    private Long deptId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableField(value = "post_id")
    @Column(name = "post_id")
    private Long postId;

    @NotNull
    @ColumnDefault("1")
    @TableField(value = "work_status")
    @Column(name = "work_status")
    private Integer workStatus = 1;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @TableField(value = "")
    @Column(name = "deleted")
    private Boolean deleted = false;

    @TableField(exist = false)
    @Transient
    private SysUser user;

//    @Transient
//    private String username;

    @TableField(exist = false)
    @Transient
    private SysDept dept;

//    @Transient
//    private String deptName;

    @TableField(exist = false)
    @Transient
    private SysPost post;

//    @Transient
//    private String postName;

    @Override
    public Boolean getDeleted() {
        return deleted;
    }
}
