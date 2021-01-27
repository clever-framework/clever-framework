/*
package io.github.toquery.framework.data.rest.config;

import com.google.common.collect.Lists;
import io.github.toquery.framework.data.rest.AppHandlerMethodReturnValueHandler;
import io.github.toquery.framework.data.rest.AppLinkCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerAdapter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.convert.UriListHttpMessageConverter;
import org.springframework.data.rest.webmvc.mapping.LinkCollector;
import org.springframework.format.FormatterRegistry;
import org.springframework.hateoas.server.mvc.TypeConstrainedMappingJackson2HttpMessageConverter;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Arrays;
import java.util.List;

*/
/**
 * 自定义RepositoryRestMvc 配置，重载linkCollector方法，移除元素上自带的 _link 属性
 *
 * @author toquery
 * @version 1
 *//*


@Slf4j
@Configuration
public class AppRepositoryRestMvcConfiguration extends RepositoryRestMvcConfiguration {


    public AppRepositoryRestMvcConfiguration(ApplicationContext context, ObjectFactory<ConversionService> conversionService) {
        super(context, conversionService);
        log.info("初始化自定义 Repository Rest Mvc 配置");
    }

    @Bean
    @Override
    protected LinkCollector linkCollector() {
        return new AppLinkCollector(persistentEntities(), selfLinkProvider(), associationLinks());
    }

    @Override
    public UriListHttpMessageConverter uriListHttpMessageConverter() {
        return super.uriListHttpMessageConverter();
    }


    */
/*
    @Override
    public MessageSourceAccessor resourceDescriptionMessageSourceAccessor() {
        return super.resourceDescriptionMessageSourceAccessor();
    }
    *//*


    @Override
    public TypeConstrainedMappingJackson2HttpMessageConverter jacksonHttpMessageConverter() {
        return super.jacksonHttpMessageConverter();
    }

    @Override
    public TypeConstrainedMappingJackson2HttpMessageConverter halJacksonHttpMessageConverter() {
        return super.halJacksonHttpMessageConverter();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        super.addResourceHandlers(registry);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        super.addFormatters(registry);
    }

    @Override
    public RequestMappingHandlerAdapter repositoryExporterHandlerAdapter() {

        // Forward conversion service to handler adapter
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        initializer.setConversionService(defaultConversionService());

        RepositoryRestHandlerAdapter handlerAdapter = new RepositoryRestHandlerAdapter(defaultMethodArgumentResolvers());
        handlerAdapter.setWebBindingInitializer(initializer);
        handlerAdapter.setMessageConverters(defaultMessageConverters());

        if (repositoryRestConfiguration().getMetadataConfiguration().alpsEnabled()) {
            handlerAdapter.setResponseBodyAdvice(Arrays.<ResponseBodyAdvice<?>> asList(alpsJsonHttpMessageConverter()));
        }
        List<HandlerMethodReturnValueHandler> list = handlerAdapter.getCustomReturnValueHandlers();
        handlerAdapter.setReturnValueHandlers(Lists.newArrayList(new AppHandlerMethodReturnValueHandler()));
        return handlerAdapter;
    }

//    @Override
//    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
//        handlers.add(new AppHandlerMethodReturnValueHandler());
//        log.info("--");
//    }
}
*/
