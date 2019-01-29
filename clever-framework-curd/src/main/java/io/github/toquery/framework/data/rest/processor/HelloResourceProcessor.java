package io.github.toquery.framework.data.rest.processor;

import com.alibaba.fastjson.JSON;
//import com.toquery.framework.demo.entity.TbJpaDemo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

/**
 * @author toquery
 * @version 1

@Slf4j
@Component
public class HelloResourceProcessor implements ResourceProcessor<Resource<TbJpaDemo>> {

    @Autowired
    private EntityLinks entityLinks;

    @Override
    public Resource<TbJpaDemo> process(Resource<TbJpaDemo> resource) {
        TbJpaDemo person = resource.getContent();
//        if (person.getId() != null) {
//            resource.add(entityLinks.linkToCollectionResource(TbJpaDemo.class)
//                    .withRel("father"));
//        }
        resource.removeLinks();
        log.info("正在处理实体，{}", JSON.toJSONString(resource.getLinks()));
        return resource;
    }
} */
