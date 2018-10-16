package io.github.toquery.framework.demo;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * @author toquery
 * @version 1
 */
//@ComponentScan(nameGenerator = DemoApplication.SpringBeanNameGenerator.class)
@EnableJpaRepositories(basePackages = {"io.github.toquery.framework"})
@SpringBootApplication
public class DemoApplication{ // extends SpringBootServletInitializer {
    /*@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(DemoApplication.class);
    }


    public static class SpringBeanNameGenerator extends AnnotationBeanNameGenerator {
        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            return definition.getBeanClassName();
        }
    }
*/
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
