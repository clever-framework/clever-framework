package io.github.toquery.framework.webmvc.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 */
@Data
public class CaptchaValidRequest {

    @NotBlank
    private String captchaValue;

    @NotBlank
    private String captchaToken;
}
