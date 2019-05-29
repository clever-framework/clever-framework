package io.github.toquery.framework.security.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toquery.framework.core.constant.AppPropertiesDefault;
import io.github.toquery.framework.dao.entity.AppBaseEntityPrimaryKeyLong;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

/**
 * A user.
 */
@Entity
@Getter
@Setter
@Table(name = "sys_user")
public class SysUser extends AppBaseEntityPrimaryKeyLong {

    @NotBlank
    @Size(min = 1, max = 50)
    @Column(name = "login_name", length = 50, unique = true, nullable = false)
    private String loginName;

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "user_name", length = 50, unique = true)
    private String userName;

    @JsonIgnore
    @NotBlank
    @Size(min = 4, max = 100)
    @Column(name = "password", length = 100)
    private String password;

    @Email
    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @NotNull
    @Column(name = "enabled")
    private Boolean enabled = true;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_password_reset_date")
    @JsonFormat(pattern = AppPropertiesDefault.DATE_TIME_PATTERN, timezone = "GMT+8")
    private Date lastPasswordResetDate = new Date();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})

    @BatchSize(size = 20)
    private Collection<SysRole> roles = new HashSet<>();


    public boolean getEnabled() {
        return enabled == null ? true : enabled;
    }

}
