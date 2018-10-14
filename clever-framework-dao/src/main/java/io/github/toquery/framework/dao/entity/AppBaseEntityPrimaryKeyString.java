package io.github.toquery.framework.dao.entity;

import com.google.common.base.Strings;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

/**
 * 定义主键为 String 类型，指定主键生成规则
 */
@Setter
@Getter
@MappedSuperclass
@Access(AccessType.FIELD)
public class AppBaseEntityPrimaryKeyString extends AppBaseEntity {

    @Id
    @NotBlank
    @Column(length = 32)
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.dao.entity.primary.AppJpaEntityStringIDGenerator")
    protected String id;

}
