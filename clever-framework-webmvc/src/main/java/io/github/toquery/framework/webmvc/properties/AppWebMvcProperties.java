package io.github.toquery.framework.webmvc.properties;

import io.github.toquery.framework.core.captcha.AbstractCaptchaService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
@Slf4j
//@Configuration
@ConfigurationProperties(prefix = AppWebMvcProperties.PREFIX)
public class AppWebMvcProperties {

    public AppWebMvcProperties() {
        log.info("AppWebMvcProperties");
    }

    public static final String PREFIX = "app.web-mvc";

    private AppWebMvcCaptchaProperties captcha = new AppWebMvcCaptchaProperties();

    private AppWebParamProperties param = new AppWebParamProperties();

    private AppWebParamDefaultProperties defaultValue = new AppWebParamDefaultProperties();

    @Getter
    @Setter
    public static class AppWebMvcCaptchaProperties {

        /**
         * 生成验证码随机的字符串
         */
        private String randomCode = AbstractCaptchaService.CAPTCHA_CODES;

        private String salt = "clever-framework";

        private int height = 100;

        private int width = 200;
        /**
         * 过期时间（分钟）
         */
        private int expired = 1;

        private int length = 4;
    }

}
