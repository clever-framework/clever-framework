package io.github.toquery.framework.data.rest;

import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.rest.core.support.SelfLinkProvider;
import org.springframework.data.rest.webmvc.mapping.Associations;
import org.springframework.data.rest.webmvc.mapping.LinkCollector;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;

import java.util.List;

/**
 * 重载linkCollector方法，移除元素上自带的 _link 属性
 *
 * @author toquery
 * @version 1
 */
public class AppLinkCollector extends LinkCollector {

    //private Links links = new Links();

    /**
     * Creates a new {@link PersistentEntities}, {@link SelfLinkProvider} and {@link Associations}.
     *
     * @param entities         must not be {@literal null}.
     * @param linkProvider     must not be {@literal null}.
     * @param associationLinks must not be {@literal null}.
     */
    public AppLinkCollector(PersistentEntities entities, SelfLinkProvider linkProvider, Associations associationLinks) {
        super(entities, linkProvider, associationLinks);
    }

    /**
     * 获取到新的Links 对象
     *
     * @param object        原数据对象
     * @param existingLinks 已经存在的Link对象
     * @return 获取到新的Links 对象

    @Override
    public Links getLinksFor(Object object, List<Link> existingLinks) {
        return links;
    }*/
}
