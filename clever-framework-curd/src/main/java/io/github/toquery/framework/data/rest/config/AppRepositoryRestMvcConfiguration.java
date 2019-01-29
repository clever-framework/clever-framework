package io.github.toquery.framework.data.rest.config;

import io.github.toquery.framework.data.rest.AppLinkCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.rest.webmvc.mapping.LinkCollector;

/**
 * 自定义RepositoryRestMvc 配置，重载linkCollector方法，移除元素上自带的 _link 属性
 *
 * @author toquery
 * @version 1
 */

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
}
