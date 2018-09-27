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

@MappedSuperclass
@Access(AccessType.FIELD)
@Data
public class AppJpaStringIdEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "generatedkey")
    @GenericGenerator(name = "generatedkey", strategy = "io.github.toquery.framework.entity.id.AppJpaEntityStringIDGenerator")
    @Column(length = 32)
    protected String id;

    /**
     * 设置id，如果id为空字符，则id=null
     *
     */
    public void setId(String id) {
        this.id = Strings.isNullOrEmpty(id) ? null : id;
    }

}
