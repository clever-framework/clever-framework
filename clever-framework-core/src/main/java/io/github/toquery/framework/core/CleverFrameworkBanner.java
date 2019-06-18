package io.github.toquery.framework.core;

import org.springframework.boot.Banner;
import org.springframework.boot.ResourceBanner;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.io.Resource;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public class CleverFrameworkBanner extends ResourceBanner implements Banner {
    public CleverFrameworkBanner(Resource resource) {
        super(resource);
    }

    @Override
    protected List<PropertyResolver> getPropertyResolvers(Environment environment, Class<?> sourceClass) {
        List<PropertyResolver> propertyResolverList = super.getPropertyResolvers(environment, sourceClass);

        return propertyResolverList;
    }

    protected String getCleverFrameworkVersion() {
        return CleverFrameworkVersion.getVersion();
    }
}
