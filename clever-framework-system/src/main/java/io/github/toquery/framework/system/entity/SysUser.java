package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Sets;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import io.github.toquery.framework.core.entity.AppEntityLogicDel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
@TableName(value = "sys_user")
@Table(name = "sys_user")
@Where(clause = "deleted = false")
@SQLDelete(sql ="UPDATE sys_user SET deleted = true WHERE id = ?")
public class SysUser extends AppBaseEntity implements UserDetails, AppUserDetails, AppEntityLogicDel {


    // 用户名，唯一
    @NotBlank
    @Length(min = 4, max = 50)
    @TableField(value = "user_name")
    @Column(name = "user_name", length = 50, unique = true)
    private String username;

    // 用户昵称
    @NotBlank
    @Length(min = 1, max = 50)
    @TableField(value = "nick_name")
    @Column(name = "nick_name", length = 50, nullable = false)
    private String nickname;

    //@JsonIgnore
    //@NotBlank
    //@Length(min = 4, max = 100)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @TableField(value = "password")
    @Column(name = "password", length = 100, nullable = false)
    private String password;

    // @NotBlank
    // @Length(min = 11, max = 11)
    @TableField(value = "phone")
    @Column(name = "phone", length = 50)
    private String phone;

    //    @Email
//    @NotBlank
//    @Size(min = 4, max = 50)
    @TableField(value = "email")
    @Column(name = "email", length = 50)
    private String email;

    @NotNull
    @ColumnDefault("1")
    @TableField(value = "user_status")
    @Column(name = "user_status")
    private Integer userStatus = 1;


    // @Temporal(TemporalType.TIMESTAMP)
    @TableField(value = "change_password_date_time")
    @Column(name = "change_password_date_time")
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime changePasswordDateTime = LocalDateTime.now();

    @TableLogic
    @ColumnDefault("false")
    @TableField(value = "deleted")
    @Column(name = "deleted")
    private Boolean deleted = false;

    /*
    @JsonIgnoreProperties("users")
//    @JsonBackReference
//    @JsonManagedReference
//    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "role")
    private Collection<SysUserRole> roles = new HashSet<>();
    */

    @TableField(exist = false)
    @Transient
    private Collection<SysPermission> userPermissions;

    /**
     * 用户角色
     */
    @TableField(exist = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Transient
    private Collection<Long> roleIds;

    @TableField(exist = false)
    @Transient
    private Collection<SysRole> roles;

    @TableField(exist = false)
    @Transient
    private SysRole currentRole;

    @TableField(exist = false)
    @Transient
    private SysArea currentArea;

    @TableField(exist = false)
    @Transient
    private SysPermission currentPermission;

    /**
     * 用于前端的角色code
     */
    @TableField(exist = false)
    @Transient
    private Set<String> codes;

    @TableField(exist = false)
    @Transient
    private Set<? extends GrantedAuthority> authorities;


    /**
     * 角色的聚合模式，返回角色所有权限
     */
    public void complexRole() {
        if (userPermissions != null && !userPermissions.isEmpty()) {
            this.authorities = userPermissions.stream().filter(item -> item.getRole() != null).flatMap(item -> item.getRole().getMenus().stream()).collect(Collectors.toSet());
            this.codes = userPermissions.stream().filter(item -> item.getRole() != null).flatMap(item -> item.getRole().getMenus().stream().map(GrantedAuthority::getAuthority)).collect(Collectors.toSet());
        }
    }

    /**
     * 角色的隔离模式，返回当前角色下的权限
     */
    public void isolateRole(Long roleId) {
        if (userPermissions != null && !userPermissions.isEmpty()) {
            Stream<SysPermission> sysRoleStream = userPermissions.stream().filter(item -> item.getRole() != null).filter(item -> item.getRole().getMenus() != null && item.getRole().getMenus().size() > 0);
            currentPermission = roleId != null && roleId != 0 ? sysRoleStream.filter(item -> item.getId().equals(roleId)).findAny().get() : sysRoleStream.findFirst().get();
            this.setCurrentPermission(currentPermission);
            this.authorities = Sets.newHashSet(currentRole.getMenus());
            this.codes = currentRole.getMenus().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
        }
    }

    public void setCurrentPermission(SysPermission currentPermission) {
        this.currentPermission = currentPermission;
        this.currentArea = currentPermission.getArea();
        this.currentRole = currentPermission.getRole();
    }

    public boolean getEnabled(){
         return this.getUserStatus()!= null && this.getUserStatus() == 1;
    }


    /**
     * Spring 用户属性
     */
    @Override
    public boolean isAccountNonExpired() {
        return this.getEnabled();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isAccountNonLocked() {
        return this.getEnabled();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return this.getEnabled();
    }

    /**
     * Spring 用户属性
     */
    @Override
    public boolean isEnabled() {
        return this.getEnabled();
    }
}
