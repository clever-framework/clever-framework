package io.github.toquery.framework.security.system.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author toquery
 * @version 1
 */
@Data
public class ChangePassword {

    // 原密码
    @NotBlank
    private String sourcePassword;
    // 新密码
    @NotBlank
    private String rawPassword;
    // 新密码
    @NotBlank
    private String rawPasswordConfirm;
}
