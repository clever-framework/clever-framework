package io.github.toquery.framework.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.toquery.framework.common.constant.AppCommonConstant;
import io.github.toquery.framework.core.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 *
 */
@Entity
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "accountNonExpired", "accountNonLocked", "credentialsNonExpired"})
@TableName(value = "sys_user_online")
@Table(name = "sys_user_online")
public class SysUserOnline extends BaseEntity {

    // 用户昵称
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    // 用户名，唯一
    @NotBlank
    @Length(min = 4, max = 50)
    @TableField(value = "user_name")
    @Column(name = "user_name", length = 50) // , unique = true)
    private String username;

    // 用户昵称
    @NotBlank
    @Length(min = 1, max = 50)
    @TableField(value = "nick_name")
    @Column(name = "nick_name", length = 50, nullable = false)
    private String nickname;

    @Lob
    @TableField(value = "token")
    @Column(columnDefinition = "text", name = "token")
    private String token;

    @NotBlank
    @Length(min = 1, max = 50)
    @TableField(value = "device")
    @Column(name = "device", length = 50, nullable = false)
    private String device;


    @TableField(value = "issuer_date")
    @Column(name = "issuer_date")
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime issuerDate;

    @TableField(value = "expires_date")
    @Column(name = "expires_date")
    @DateTimeFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN, iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(pattern = AppCommonConstant.DATE_TIME_PATTERN)
    private LocalDateTime expiresDate;
}
