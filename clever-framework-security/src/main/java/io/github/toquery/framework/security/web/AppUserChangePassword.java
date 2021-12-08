package io.github.toquery.framework.security.web;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author toquery
 * @version 1
 */
@Data
public class AppUserChangePassword {

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
