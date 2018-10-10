package io.github.toquery.framework.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;


/**
 * 定义主键为Long类型，指定主键生成规则
 */
@Data
@MappedSuperclass
@Access(AccessType.FIELD)
public class AppBaseEntityPrimaryKeyLong implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.primary.generator.AppJpaEntityLongIDGenerator")
    @Column
    protected Long id;

}
