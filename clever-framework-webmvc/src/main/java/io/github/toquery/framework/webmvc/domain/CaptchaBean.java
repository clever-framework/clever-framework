package io.github.toquery.framework.webmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 */
@Data
@AllArgsConstructor
public class CaptchaBean {
    private String token;
    private String image;
}
