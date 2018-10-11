package io.github.toquery.framework.demo.entity;

import io.github.toquery.framework.entity.AppBaseEntityLong;
import lombok.Getter;
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
@Table(name = "tb_jpa_demo")
public class TbJpaDemo extends AppBaseEntityLong {

    @Column
    private String name;

    @Column(name = "login_time")
    private Date loginTime;
}
