package io.github.toquery.framework.data.rest;

import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.support.SelfLinkProvider;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.data.rest.webmvc.mapping.LinkCollector;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

/**
 * 重载linkCollector方法，移除元素上自带的 _link 属性
 *
 * @author toquery
 * @version 1
 */
public class AppLinkCollector implements LinkCollector {

    @Override
    public Links getLinksFor(Object object) {
        return null;
    }

    @Override
    public Links getLinksFor(Object object, Links existing) {
        return null;
    }

    @Override
    public Links getLinksForNested(Object object, Links existing) {
        return null;
    }
}
