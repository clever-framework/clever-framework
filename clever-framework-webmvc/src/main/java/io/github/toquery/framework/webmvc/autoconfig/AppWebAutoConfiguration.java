package io.github.toquery.framework.webmvc.autoconfig;

import io.github.toquery.framework.webmvc.config.AppWebProperties;
import io.github.toquery.framework.webmvc.configurer.AppWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author toquery
 * @version 1
 */
@Configuration
@ConditionalOnWebApplication
@ComponentScan(basePackages = "io.github.toquery.framework.webmvc")
@Import(value = {AppWebProperties.class, AppWebMvcConfigurer.class})
public class AppWebAutoConfiguration {
}
