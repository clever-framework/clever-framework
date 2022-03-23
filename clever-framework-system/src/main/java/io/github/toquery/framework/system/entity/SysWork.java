package io.github.toquery.framework.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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

/**
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_work")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_work SET deleted = true WHERE id = ?")
public class SysWork extends AppBaseEntity implements AppEntityLogicDel {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "user_id")
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "dept_id")
    private Long deptId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "post_id")
    private Long postId;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "work_status")
    private Integer workStatus = 1;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    @Transient
    private SysUser user;

//    @Transient
//    private String username;

    @Transient
    private SysDept dept;

//    @Transient
//    private String deptName;

    @Transient
    private SysPost post;

//    @Transient
//    private String postName;

    @Override
    public boolean getDeleted() {
        return deleted;
    }
}
