package io.github.toquery.framework.entity;

import com.google.common.base.Strings;
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
 * 定义主键为 String 类型，指定主键生成规则
 */
@Data
@MappedSuperclass
@Access(AccessType.FIELD)
public class AppBaseEntityPrimaryKeyString implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.entity.primary.AppJpaEntityStringIDGenerator")
    protected String id;

    /**
     * 设置id，如果id为空字符，则id=null
     */
    public void setId(String id) {
        this.id = Strings.isNullOrEmpty(id) ? null : id;
    }

}
