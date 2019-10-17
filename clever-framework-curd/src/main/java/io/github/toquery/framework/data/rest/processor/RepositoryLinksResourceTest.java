package io.github.toquery.framework.data.rest.processor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryLinksResource;
//import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
//import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * @author toquery
 * @version 1

@Component
public class RepositoryLinksResourceTest implements ResourceProcessor<RepositoryLinksResource> {
    @Autowired
    private  EntityLinks entityLinks;

    @Override
    public RepositoryLinksResource process(RepositoryLinksResource resource) {
        resource.getLinks(); //the root links
        return resource;
    }
} */
