package io.github.toquery.framework.webmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 */
@Data
@AllArgsConstructor
public class CaptchaResponse {
    private String captchaToken;
    private String captchaImage;
}
