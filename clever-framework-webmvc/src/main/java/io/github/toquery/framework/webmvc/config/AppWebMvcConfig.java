package io.github.toquery.framework.webmvc.config;

import io.github.toquery.framework.webmvc.StringToEnumIgnoringCaseConverterFactory;
import io.github.toquery.framework.webmvc.resolver.MethodArgumentUpperCaseResolver;
import io.github.toquery.framework.webmvc.resolver.PathVariableMethodArgumentUpperCaseResolver;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

    public AppWebMvcConfig() {
        log.info("初始化 App Web Mvc 配置 {}", this.getClass().getSimpleName());
    }

    /**
     * 依据Spring的适配器，使用初始化顺序调用解析
     *
     * @see org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter
     * 如果接收参数使用其他注解处理，则只会使用其他的解析器
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        log.debug("获取到原始解析器 {} 个", resolvers.size());
        resolvers.add(getMethodArgumentUpperCaseResolver());
        log.debug("增加自定义解析器 MethodArgumentUpperCaseResolver ");
        resolvers.add(getPathVariableMethodArgumentUpperCaseResolver());
        log.debug("增加自定义解析器 PathVariableMethodArgumentUpperCaseResolver ");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new StringToEnumIgnoringCaseConverterFactory());

//        registry.addConverterFactory();
//        ApplicationConversionService.configure(registry);
    }

    @Bean
    public HandlerMethodArgumentResolver getMethodArgumentUpperCaseResolver() {
        return new MethodArgumentUpperCaseResolver();
    }

    @Bean
    public HandlerMethodArgumentResolver getPathVariableMethodArgumentUpperCaseResolver() {
        return new PathVariableMethodArgumentUpperCaseResolver();
    }

   /* 兼容新版 2.4.x spring boot
   @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    */

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("app - cors 开启");
        //添加映射路径
        registry.addMapping("/**")
                //放行哪些原始域
//                .allowedOrigins("*")
                .allowedOriginPatterns("*")
                //是否发送Cookie信息
                .allowCredentials(true)
                //放行哪些原始域(请求方式)
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                //放行哪些原始域(头部信息)
                .allowedHeaders("*");
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        log.info("初始化表单实体验证,验证类提供者{}", HibernateValidator.class.getName());
        return validatorFactoryBean;
    }
}
