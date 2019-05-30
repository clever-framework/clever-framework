package com.toquery.framework.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toquery.framework.dao.entity.AppBaseEntityPrimaryKeyLong;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;
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
@Table(name = "demo_org")
public class DemoOrg extends AppBaseEntityPrimaryKeyLong {

    @NotBlank
    @Size(min = 4, max = 50)
    @Column(name = "org_name", length = 50, unique = true)
    private String orgName;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "demo_user_org",
            joinColumns = {@JoinColumn(name = "org_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Collection<DemoUser> users = new HashSet<>();
}
