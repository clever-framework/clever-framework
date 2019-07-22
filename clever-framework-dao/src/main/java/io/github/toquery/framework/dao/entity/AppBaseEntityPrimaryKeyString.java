package io.github.toquery.framework.dao.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * 定义主键为 String 类型，指定主键生成规则
 * @deprecated 2019.07.01 以后统一使用主键为long型
 */
@Setter
@Getter
@Deprecated
@MappedSuperclass
@Access(AccessType.FIELD)
@EqualsAndHashCode(callSuper = true)
public class AppBaseEntityPrimaryKeyString extends AppBaseEntity {

//    @Id
//    @NotBlank
//    @Column(length = 32)
//    @GeneratedValue(generator = "generatedkey")
//    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.dao.entity.primary.AppJpaEntityStringIDGenerator")
//    protected String id;

}
