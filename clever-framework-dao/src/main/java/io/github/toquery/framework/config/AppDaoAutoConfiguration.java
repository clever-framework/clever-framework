package io.github.toquery.framework.config;

import io.github.toquery.framework.config.jpa.AppJPAAutoConfiguration;
import io.github.toquery.framework.mybatis.AppMybatisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Slf4j
@Configuration
@EnableConfigurationProperties(JpaProperties.class)
@Import(value = {AppJPAAutoConfiguration.class, AppMybatisAutoConfiguration.class})
public class AppDaoAutoConfiguration {


}
