package io.github.toquery.framework.webmvc.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.github.toquery.framework.webmvc.resolver.MethodArgumentUpperCaseResolver;
import io.github.toquery.framework.webmvc.resolver.PathVariableMethodArgumentUpperCaseResolver;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
public class AppWebMvcConfig implements WebMvcConfigurer {

    public AppWebMvcConfig() {
        log.info("初始化 App Web Mvc 配置 {}", this.getClass().getSimpleName());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        log.debug("获取到原始解析器 {} 个", resolvers.size());
        resolvers.add(new MethodArgumentUpperCaseResolver());
        log.debug("增加自定义解析器 MethodArgumentUpperCaseResolver ");
        resolvers.add(new PathVariableMethodArgumentUpperCaseResolver());
        log.debug("增加自定义解析器 PathVariableMethodArgumentUpperCaseResolver ");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


    /**
     * 前台使用long类型数据时，因位数不足，会将末两位转换为00，所以需要将long转换为String
     *
     * @param converters 已经注册的MessageConverter集合
     */
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        for (HttpMessageConverter<?> httpMessageConverter : converters) {
            log.info("添加 HttpMessageConverter : {}", httpMessageConverter.getClass().getName());
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) httpMessageConverter).getObjectMapper();
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                objectMapper.registerModule(simpleModule);
                log.info("向 MappingJackson2HttpMessageConverter 添加Long类型转换规则");
            }
        }
    }

    @Override
    public Validator getValidator() {
        LocalValidatorFactoryBean validatorFactoryBean = new LocalValidatorFactoryBean();
        validatorFactoryBean.setProviderClass(HibernateValidator.class);
        log.info("初始化表单实体验证,验证类提供者{}", HibernateValidator.class.getName());
        return validatorFactoryBean;
    }
}
