package io.github.toquery.framework.mybatis;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import io.github.toquery.framework.mybatis.meta.JPAObjectWrapperFactory;
import io.github.toquery.framework.mybatis.page.support.PageInterceptor;
import io.github.toquery.framework.mybatis.support.MybatisRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
 /*
@Slf4j
@ConditionalOnBean(DataSource.class)
@AutoConfigureOrder(Ordered.LOWEST_PRECEDENCE)
@org.springframework.context.annotation.Configuration
@ConditionalOnProperty(prefix = "spring.mybatis", name = "enabled", matchIfMissing = true, havingValue = "true")
public class AppMybatisAutoConfiguration {


    public AppMybatisAutoConfiguration() {
        log.info("初始化{}", this.getClass().getSimpleName());
    }

   @Bean
    public SqlSessionFactoryBean sessionFactoryBean(DataSource dataSource) throws IOException {

        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        //别名包
        if (StringUtils.isNotBlank(appDaoProperties.getMybatis().getAliasPackage())) {
            sqlSessionFactoryBean.setTypeAliasesPackage(appDaoProperties.getMybatis().getAliasPackage());
        }

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        //获取mybatis映射文件路径，classpath:mybatis/mybatis-config.xml
        if (StringUtils.isNotBlank(appDaoProperties.getMybatis().getConfig())) {
            sqlSessionFactoryBean.setConfigLocation(resolver.getResource(appDaoProperties.getMybatis().getConfig()));
        }
        //获取mybatis映射文件路径，例：classpath:mapper/*.xml
        List<String> locations = appDaoProperties.getMybatis().getMapperLocations();
        if (locations != null && locations.size() > 0) {
            Map<String, Resource> resourceMap = Maps.newHashMap();
            for (String location : locations) {
                Resource[] tmpResources = resolver.getResources(location);
                for (Resource tmpResource : tmpResources) {
                    resourceMap.put(tmpResource.getURI().toString(), tmpResource);
                }
            }
            log.info("mybatis映射文件列表{}", Joiner.on("\n").join(resourceMap.keySet()));
            sqlSessionFactoryBean.setMapperLocations(resourceMap.values().toArray(new Resource[]{}));
        } else {
            log.warn("mybatis映射文件列表为空");
        }

        //开启后将在启动时检查设定的parameterMap,resultMap是否存在，是否合法
        sqlSessionFactoryBean.setFailFast(true);

        //存储mybatis的插件
        List<Interceptor> interceptorList = new ArrayList<Interceptor>();
        //初始化mybatis的插件
        initPlugins(interceptorList);
        //设置mybatis插件
        sqlSessionFactoryBean.setPlugins(interceptorList.toArray(new Interceptor[]{}));

        //创建自定义的的Configuration
        Configuration configuration = new Configuration();
        //查询集合中字段值为null的时候也在查询结果中显示对应的列
        configuration.setCallSettersOnNulls(true);

        //初始化jpa类属性和列的查找方式
        configuration.setObjectWrapperFactory(new JPAObjectWrapperFactory());

        sqlSessionFactoryBean.setConfiguration(configuration);

        return sqlSessionFactoryBean;
    }

    *//**
     * MapperScannerConfigurer继承BeanDefinitionRegistryPostProcessor，需要在对象创建时就需要获取相关实例对象，
     * 因此方法修饰符为static
     *
     * @return
     *//*
    @Bean
    public static MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer mapperScannerConfigurer = new MapperScannerConfigurer();
        //由配置文件中获取mybatis映射DAO的路径
        mapperScannerConfigurer.setBasePackage(Joiner.on(",").join(AppProperties.AppPackage.BASE_PACKAGES));
        //mybatis的接口必须集成mybatis接口的标识接口
        mapperScannerConfigurer.setAnnotationClass(MybatisRepository.class);

        return mapperScannerConfigurer;
    }

    *//**
     * 初始化分页插件
     *
     * @param interceptorList
     *//*
    protected void initPagePlugin(List<Interceptor> interceptorList) {
        //初始化mybatis的分页插件
        PageInterceptor pageHelper = new PageInterceptor();
        Properties properties = new Properties();
        properties.put("dialect", appDaoProperties.getMybatis().getDialect());
        //不进行合理化查询优化
        properties.put("reasonable", "false");
        pageHelper.setProperties(properties);
        //添加分页插件
        interceptorList.add(pageHelper);
    }

    *//**
     * 初始化mybatis的插件
     *//*
    protected void initPlugins(List<Interceptor> interceptorList) {
        initPagePlugin(interceptorList);
    }

}
*/