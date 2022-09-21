package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_work SET deleted = true WHERE id = ?")
public class SysWork extends AppBaseEntity implements AppEntityLogicDel {

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
