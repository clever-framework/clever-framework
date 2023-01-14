package io.github.toquery.framework.mybatis.plus.autoconfig;


import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.toquery.framework.mybatis.plus.interceptor.AppEntityAuditInnerInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppMybatisPlusAutoConfiguration {

    public AppMybatisPlusAutoConfiguration() {
        log.info("自动装配 App Mybatis Plus 自动配置");
    }


    /**
     * 分页插件
     */
    @Bean
    public InnerInterceptor paginationInnerInterceptor() {
        return new PaginationInnerInterceptor();
    }


    /**
     *
     */
    @Bean
    public InnerInterceptor appEntityAuditInterceptor() {
        return new AppEntityAuditInnerInterceptor();
    }

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> interceptors) {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptors.forEach(interceptor::addInnerInterceptor);
        return interceptor;
    }

    /**
     * 新的分页插件,一缓和二缓遵循mybatis的规则,需要设置 MybatisConfiguration#useDeprecatedExecutor = false 避免缓存出现问题(该属性会在旧插件移除后一同移除)
     */
    @Bean
    @ConditionalOnMissingBean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> configuration.setUseGeneratedKeys(false);
    }
}
