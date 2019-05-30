package com.toquery.framework.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toquery.framework.core.constant.AppPropertiesDefault;
import io.github.toquery.framework.dao.entity.AppBaseEntityPrimaryKeyLong;
import io.github.toquery.framework.security.domain.SysMenu;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
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
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "demo_user")
public class DemoUser extends AppBaseEntityPrimaryKeyLong {

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "user_name", length = 50, unique = true)
    private String userName;

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
    private Date birthday = new Date();

    @NotBlank
    @Lob
    @Column
    private String content;

    @NotNull
    @Column
    private Integer height;



    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "demo_user_org",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "org_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Collection<DemoOrg> orgs = new HashSet<>();
}
