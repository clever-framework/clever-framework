package io.github.toquery.framework.security.domain;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author toquery
 * @version 1
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_menu")
public class SysMenu extends AppBaseEntityPrimaryKeyLong {

    public SysMenu(@NotNull @Size(max = 50) String name, @NotNull @Size(max = 50) String code) {
        this.name = name;
        this.code = code;
    }

    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String code;


    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})

    @BatchSize(size = 20)
    private Collection<SysRole> menus = new HashSet<>();

}
