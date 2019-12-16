package io.github.toquery.framework.security.web.builders;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.SecurityBuilder;

import javax.servlet.Filter;

public class AppWebSecurity extends AbstractConfiguredSecurityBuilder<Filter, AppWebSecurity> implements SecurityBuilder<Filter>, ApplicationContextAware{

    protected AppWebSecurity(ObjectPostProcessor<Object> objectPostProcessor) {
        super(objectPostProcessor);
    }

    protected AppWebSecurity(ObjectPostProcessor<Object> objectPostProcessor, boolean allowConfigurersOfSameType) {
        super(objectPostProcessor, allowConfigurersOfSameType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    @Override
    protected Filter performBuild() throws Exception {
        return null;
    }
}
