package io.github.toquery.framework.demo.entity;

import io.github.toquery.framework.dao.entity.AppBaseEntityPrimaryKeyLong;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_jpa_demo")
public class TbJpaDemo extends AppBaseEntityPrimaryKeyLong {

    @Column
    private String name;

    @Column(name = "login_time")
    private Date loginTime;
}
