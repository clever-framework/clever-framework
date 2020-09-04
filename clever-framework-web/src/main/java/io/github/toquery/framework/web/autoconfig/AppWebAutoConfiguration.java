package io.github.toquery.framework.web.autoconfig;

//import io.github.toquery.framework.web.dict.AppDictScannerConfigurer;
//import io.github.toquery.framework.web.dict.DefinitionRegistryPostProcessor;

import io.github.toquery.framework.web.properties.AppWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
@EnableConfigurationProperties({AppWebProperties.class})
@ConditionalOnProperty(prefix = AppWebProperties.PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class AppWebAutoConfiguration {
    public AppWebAutoConfiguration() {
        log.info("初始化 App Web 自动配置");
    }

//    @Bean
//    public AppDictScannerConfigurer getAppDictScannerConfigurer(){
//        return new AppDictScannerConfigurer();
//    }
//
//    @Bean
//    public DefinitionRegistryPostProcessor get2(){
//        return new DefinitionRegistryPostProcessor();
//    }
}
