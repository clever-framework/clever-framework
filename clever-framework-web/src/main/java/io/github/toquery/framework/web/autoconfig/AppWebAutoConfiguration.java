package io.github.toquery.framework.web.autoconfig;

//import io.github.toquery.framework.web.dict.AppDictScannerConfigurer;
//import io.github.toquery.framework.web.dict.DefinitionRegistryPostProcessor;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.github.toquery.framework.web.formatter.LocalDateFormatter;
import io.github.toquery.framework.web.formatter.LocalDatetimeFormatter;
import io.github.toquery.framework.web.properties.AppWebProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@Configuration
@EnableConfigurationProperties({AppWebProperties.class})
@ConditionalOnProperty(prefix = AppWebProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
public class AppWebAutoConfiguration {
    public AppWebAutoConfiguration() {
        log.info("自动装配 App Web 自动配置");
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

    @Bean
    @ConditionalOnMissingBean
    public Module customModule() {
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDatetimeFormatter.LocalDateTimeSerializer());
        module.addDeserializer(LocalDateTime.class, new LocalDatetimeFormatter.LocalDateTimeDeserializer());
        module.addSerializer(LocalDate.class, new LocalDateFormatter.LocalDateSerializer());
        module.addDeserializer(LocalDate.class, new LocalDateFormatter.LocalDateDeserializer());
        // module.addSerializer(Long.class, ToStringSerializer.instance);
        return module;
    }
}
