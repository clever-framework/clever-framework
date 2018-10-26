package io.github.toquery.framework.web.autoconfig;

import io.github.toquery.framework.web.configurer.AppWebMvcConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author toquery
 * @version 1
 */
@Configuration
@ConditionalOnWebApplication
@Import(AppWebMvcConfigurer.class)
public class AppWebAutoConfiguration {
}
