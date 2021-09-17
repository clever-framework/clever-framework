package io.github.toquery.framework.webmvc.domain;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 */
@Data
public class CaptchaValidReq {
    @NotBlank
    private String captcha;
    @NotBlank
    private String token;
}
