package io.github.toquery.framework.system.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 *
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
@Table(name = "sys_user_online")
public class SysUserOnline extends AppBaseEntity {

    // 用户昵称
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    // 用户名，唯一
    @NotBlank
    @Length(min = 4, max = 50)
    @Column(name = "user_name", length = 50, unique = true)
    private String username;

    // 用户昵称
    @NotBlank
    @Length(min = 1, max = 50)
    @Column(name = "nick_name", length = 50, nullable = false)
    private String nickname;

    @Length(min = 1, max = 50)
    @Column(name = "token", length = 50, nullable = false)
    private String token;

    @NotBlank
    @Length(min = 1, max = 50)
    @Column(name = "device", length = 50, nullable = false)
    private String device;


    @Column(name = "issuer_date")
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime issuerDate;

    @Column(name = "expires_date")
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime expiresDate;
}
