package com.toquery.framework.example;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.toquery.framework.dao.EnableAppJpaRepositories;
import io.github.toquery.framework.dao.interceptor.AppEntityAuditInnerInterceptor;
import io.github.toquery.framework.dao.interceptor.AppEntityAuditInterceptor;
import io.github.toquery.framework.web.dict.annotation.AppDictScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

/**
 * @author toquery
 * @version 1
 */
//@Import(AppRepositoryRestMvcConfiguration.class)
//@SpringBootApplication  (exclude = RepositoryRestMvcAutoConfiguration.class)
//@EntityScan // (basePackages = {"io.github.toquery.framework.security.domain", "com.toquery.framework.demo.entity"})
//@EnableAppJpaRepositories //(basePackages = {"io.github.toquery.framework.security", "com.toquery.framework.demo"})
//@EnableAppRepositoryRest
//@GRpcServerScan(basePackages = "com.toquery.framework.example.grpc.server")
@MapperScan("com.toquery.framework.example.**.dao")
@AppDictScan
@EnableCaching
//@AppDictScan(basePackage = "com.toquery.framework.demo.constant")
@EntityScan
//@EnableTransactionManagement
@EnableAppJpaRepositories
@SpringBootApplication(nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class)
//@EnableAspectJAutoProxy(proxyTargetClass = true)
//(scanBasePackages = "com.toquery.framework.demo")
public class CleverFrameworkDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(CleverFrameworkDemoApplication.class, args);
    }

    /**
     *  mybatis-plus 插件集合
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        //  插件
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        AppEntityAuditInnerInterceptor appEntityAuditInterceptor = new AppEntityAuditInnerInterceptor();
        interceptor.addInnerInterceptor(appEntityAuditInterceptor);
        // 添加分页插件
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

}
