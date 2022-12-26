package io.github.toquery.framework.security.model.request;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @author toquery
 * @version 1
 */
@Data
public class UserChangePasswordRequest {

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
