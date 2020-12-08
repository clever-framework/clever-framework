package io.github.toquery.framework.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_area")
public class SysArea extends AppBaseEntity {

    /**
     * 名称
     */
    @NotBlank
    @Length(min = 4, max = 100)
    @Column(name = "name", length = 100)
    private String name;

    /**
     * 编码
     */
    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String code;

    @Column(name = "level")
    private int level = 0;

    /**
     * 上级编码
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 上级编码
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 上级编码
     */
    @Column(name = "parent_ids")
    private String parentIds;

    /**
     * 上级编码列表
     */
    @Column(name = "parent_codes")
    private String parentCodes;


    @Column(name = "sort_num")
    private Integer sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = "deleted")
    private boolean deleted = false;

    @JsonIgnoreProperties({"scope", "lastUpdateDatetime", "createDatetime"})
    @OneToMany // (cascade = CascadeType.ALL)
    @JoinTable(
            name = "sys_role_area",
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
            joinColumns = {@JoinColumn(name = "area_id", referencedColumnName = "id")})
    private Collection<SysRole> roles = new HashSet<>();

    /**
     * 子集
     */
    @Transient
    private List<SysArea> children = new ArrayList<>();

}
