package io.github.toquery.framework.webmvc.autoconfig;

import io.github.toquery.framework.webmvc.advice.AppResponseWrapAdvice;
import io.github.toquery.framework.webmvc.config.AppWebMvcConfig;
import io.github.toquery.framework.webmvc.controller.AppCaptchaRest;
import io.github.toquery.framework.webmvc.controller.AppDictRest;
import io.github.toquery.framework.webmvc.controller.advice.AppExceptionAdvice;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import io.github.toquery.framework.webmvc.secret.advice.AppRequestSecretAdvice;
import io.github.toquery.framework.webmvc.secret.advice.AppResponseSecretAdvice;
import io.github.toquery.framework.webmvc.service.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
@ConditionalOnWebApplication
@Import(AppWebMvcConfig.class)
//@ComponentScan(basePackages = "io.github.toquery.framework.webmvc")
@EnableConfigurationProperties(value = {AppWebMvcProperties.class})
public class AppWebMvcAutoConfiguration {

    public AppWebMvcAutoConfiguration() {
        log.info("自动装配 App Web Mvc 自动化配置");
    }

    @Bean
    @ConditionalOnMissingClass
    public CaptchaService getCaptchaService() {
        return new CaptchaService();
    }

    @Bean
    @ConditionalOnMissingClass
    public AppCaptchaRest getAppCaptchaRest() {
        return new AppCaptchaRest();
    }

    @Bean
    public AppDictRest getAppDictRest() {
        return new AppDictRest();
    }

    @Bean
    public AppExceptionAdvice getAppExceptionAdvice() {
        return new AppExceptionAdvice();
    }

    @Bean
    @ConditionalOnProperty(prefix = AppWebMvcProperties.PREFIX, name = "wrap-response", havingValue = "true", matchIfMissing = false)
    public AppResponseWrapAdvice getAppResponseWrapAdvice() {
        return new AppResponseWrapAdvice();
    }


    @Bean
//    @ConditionalOnProperty(prefix = AppWebMvcProperties.PREFIX + ".secret.request", name = "enabled", havingValue = "true")
    public AppRequestSecretAdvice getAppRequestSecretAdvice() {
        return new AppRequestSecretAdvice();
    }

    @Bean
//    @ConditionalOnProperty(prefix = AppWebMvcProperties.PREFIX + ".secret.response", name = "enabled", havingValue = "true")
    public AppResponseSecretAdvice getAppResponseSecretAdvice() {
        return new AppResponseSecretAdvice();
    }

    /*
    @Bean
    public AppErrorController getAppErrorController(){
        return new AppErrorController();
    }
     */
}


