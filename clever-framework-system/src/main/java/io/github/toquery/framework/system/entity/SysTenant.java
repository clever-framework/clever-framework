package io.github.toquery.framework.system.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.toquery.framework.common.constant.EntityFieldConstant;
import io.github.toquery.framework.core.domain.EntitySort;
import io.github.toquery.framework.core.entity.BaseEntity;
import io.github.toquery.framework.core.entity.EntityLogicDel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.validator.constraints.Length;

import java.util.Collection;

/**
 * An authority (a security role) used by Spring Security.
 */
@Entity
@Getter
@Setter
@TableName(value = "sys_tenant")
@Table(name = "sys_tenant")
@SQLRestriction(value = EntityFieldConstant.DOMAIN_FIELD_SOFT_DEL + " = false")
@SQLDelete(sql ="UPDATE sys_tenant SET deleted = true WHERE id = ?")
public class SysTenant extends BaseEntity implements EntityLogicDel, EntitySort {


    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "tenant_name", length = 50, unique = true)
    private String tenantName;

    @NotBlank
    @Length(min = 2, max = 50)
    @Column(name = "tenant_code", length = 50, unique = true)
    private String tenantCode;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "tenant_status")
    private Integer tenantStatus = 1;

    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private Boolean deleted = false;

}
