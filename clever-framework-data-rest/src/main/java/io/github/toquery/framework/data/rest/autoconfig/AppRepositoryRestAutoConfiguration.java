package io.github.toquery.framework.data.rest.autoconfig;

import io.github.toquery.framework.data.rest.AppEntityRestController;
import io.github.toquery.framework.data.rest.annotation.EnableAppRepositoryRest;
import io.github.toquery.framework.data.rest.mapping.AppEntityRestRequestMappingHandlerMapping;
import io.github.toquery.framework.data.rest.mapping.AppRepositoryRestHandlerMapping;
import io.github.toquery.framework.data.rest.properties.AppDataRestProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.RepositoryResourceMappings;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@EnableAppRepositoryRest
@EnableConfigurationProperties(AppDataRestProperties.class)
@ConditionalOnProperty(prefix = AppDataRestProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppRepositoryRestAutoConfiguration {

    public AppRepositoryRestAutoConfiguration() {
        log.info("初始化 Data Rest 自动化配置");
    }


    @Bean
    public AppEntityRestController getAppEntityRestController(){
        return new AppEntityRestController();
    }

    @Autowired
    private PersistentEntities persistentEntities;

    @Autowired
    private Repositories repositories;

    @Autowired
    private RepositoryResourceMappings resourceMappings;

    @Autowired
    private RepositoryRestConfiguration repositoryRestConfiguration;

//    @Bean
//    public AppEntityRestRequestMappingHandlerMapping getAppEntityRestRequestMappingHandlerMapping() {
//        return new AppEntityRestRequestMappingHandlerMapping(resourceMappings, repositoryRestConfiguration, repositories);
//    }
//
//    @Bean
//    public AppRepositoryRestHandlerMapping getAppRepositoryRestHandlerMapping() {
//        return new AppRepositoryRestHandlerMapping(repositories, persistentEntities, repositoryRestConfiguration);
//    }

}
