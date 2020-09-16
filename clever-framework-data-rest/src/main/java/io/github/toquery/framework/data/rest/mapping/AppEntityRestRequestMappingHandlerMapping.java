package io.github.toquery.framework.data.rest.mapping;

import io.github.toquery.framework.data.rest.annotation.AppEntityRest;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.CollectionResourceMapping;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.core.mapping.RepositoryResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMappings;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping;
import org.springframework.data.util.ProxyUtils;
import org.springframework.data.util.TypeInformation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.util.Assert;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * org.springframework.data.rest.webmvc.RepositoryRestHandlerMapping
 */
public class AppEntityRestRequestMappingHandlerMapping extends RepositoryRestHandlerMapping {


    private boolean useSuffixPatternMatch = true;

    private boolean useTrailingSlashMatch = true;


    private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();

    private final List<String> fileExtensions = new ArrayList<String>();

    private static AppEntityRest requestMapping = (AppEntityRest) AppEntityRestRequestMappingHandlerMapping.class
            .getAnnotation(AppEntityRest.class);

    public AppEntityRestRequestMappingHandlerMapping(ResourceMappings mappings, RepositoryRestConfiguration config) {
        super(mappings, config);
    }

    public AppEntityRestRequestMappingHandlerMapping(ResourceMappings mappings, RepositoryRestConfiguration config, Repositories repositories) {
        super(mappings, config, repositories);
    }


    @Override
    protected boolean isHandler(Class<?> beanType) {
        Class<?> type = ProxyUtils.getUserClass(beanType);
        return AnnotationUtils.findAnnotation(type, AppEntityRest.class) != null;
    }



    @Override
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {


        RequestMappingInfo info = null;
        if (requestMapping != null) {
            RequestCondition<?> methodCondition = getCustomMethodCondition(method);
            //info = createRequestMappingInfo(requestMapping, methodCondition, method);
            AppEntityRest typeAnnotation = AnnotationUtils.findAnnotation(handlerType, AppEntityRest.class);
            if (typeAnnotation != null) {
                RequestCondition<?> typeCondition = getCustomTypeCondition(handlerType);
                //info = createRequestMappingInfo(typeAnnotation, typeCondition, method).combine(info);
            }
        }
        return info;
    }

//    @Override
//    protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) throws Exception {
//        Set<TypeInformation<?>> set = persistentEntities.getManagedTypes().stream().filter(item -> {
//            Optional<TypeInformation<?>> optional = persistentEntities.getManagedTypes().stream().findAny();
//            return optional.isPresent() && optional.get().getType().getAnnotation(AppEntityRest.class) != null;
//        }).collect(Collectors.toSet());
//        return super.lookupHandlerMethod(lookupPath, request);
//    }
}
