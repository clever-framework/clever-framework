package io.github.toquery.framework.web.configurer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.HibernateValidator;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Slf4j
public class AppWebMvcConfigurer implements WebMvcConfigurer {

    public AppWebMvcConfigurer() {
        log.info("创建自定义的web-mvc配置 {}", this.getClass().getSimpleName());
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
            log.info("添加HttpMessageConverter : {}", httpMessageConverter.getClass().getName());
            if (httpMessageConverter instanceof MappingJackson2HttpMessageConverter) {
                ObjectMapper objectMapper = ((MappingJackson2HttpMessageConverter) httpMessageConverter).getObjectMapper();
                SimpleModule simpleModule = new SimpleModule();
                simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
                simpleModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                objectMapper.registerModule(simpleModule);
                log.info("向MappingJackson2HttpMessageConverter添加Long类型转换规则");
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

   /*
   @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        Method getViewResolver = ReflectionUtils.findMethod(urlRegistration.getClass(), "getViewResolver");
        getViewResolver.setAccessible(true);
        try {
            UrlBasedViewResolver urlBasedViewResolver = (UrlBasedViewResolver) getViewResolver.invoke(
                    urlRegistration);
            urlBasedViewResolver.setContentType("text/html;charset=UTF-8");
            //暴漏requestContext , requestContext的实现类为org.springframework.web.servlet.task.RequestContext
            urlBasedViewResolver.setRequestContextAttribute("requestContext");
            urlBasedViewResolver.setAttributesMap(getContextParams());
            //如果是抽象模板，暴漏request和session中的属性
            if (urlBasedViewResolver instanceof AbstractTemplateViewResolver) {
                AbstractTemplateViewResolver abstractTemplateViewResolver = (AbstractTemplateViewResolver) urlBasedViewResolver;
                log.info("暴漏request和session的属性到请求上下文，在页面模板中可以获取request和session对象");
                abstractTemplateViewResolver.setExposeRequestAttributes(true);
                abstractTemplateViewResolver.setExposeSessionAttributes(true);
                //允许session或request中的属性重写
                abstractTemplateViewResolver.setAllowSessionOverride(true);
                abstractTemplateViewResolver.setAllowRequestOverride(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }*/
}
