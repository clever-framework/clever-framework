package io.github.toquery.framework.dao.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.RevisionNumber;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;


/**
 * 定义主键为Long类型，指定主键生成规则
 */
@Setter
@Getter
@Deprecated
@MappedSuperclass
@Access(AccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class AppBaseEntityPrimaryKeyLong extends AppBaseEntity {

    @Id
    @Column
    @RevisionNumber
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.dao.primary.generator.AppJpaEntityLongIDGenerator")
    protected Long id;

}
