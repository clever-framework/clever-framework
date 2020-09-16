package io.github.toquery.framework.data.rest.mapping;

import io.github.toquery.framework.data.rest.annotation.AppEntityRest;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.Path;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.ParameterMetadata;
import org.springframework.data.rest.core.mapping.ParametersMetadata;
import org.springframework.data.rest.core.mapping.ResourceDescription;
import org.springframework.data.rest.core.mapping.ResourceMapping;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.core.AnnotationAttribute;
import org.springframework.hateoas.server.core.MethodParameters;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * org.springframework.data.rest.core.mapping.RepositoryMethodResourceMapping
 */
public class AppEntityMethodResourceMapping implements MethodResourceMapping {

    private static final Collection<Class<?>> IMPLICIT_PARAMETER_TYPES = Arrays.asList(Pageable.class, Sort.class);
    private static final AnnotationAttribute PARAM_VALUE = new AnnotationAttribute(Param.class);

    private final boolean isExported;
    private final LinkRelation rel;
    private final Path path;
    private final Method method;
    private final boolean paging;
    private final boolean sorting;
    private final RepositoryMetadata metadata;

    private final List<ParameterMetadata> parameterMetadata;

    public AppEntityMethodResourceMapping(Method method, ResourceMapping resourceMapping, RepositoryMetadata metadata,
                                          boolean exposeMethodsByDefault) {
        Assert.notNull(method, "Method must not be null!");
        Assert.notNull(resourceMapping, "ResourceMapping must not be null!");

        AppEntityRest annotation = AnnotationUtils.findAnnotation(method, AppEntityRest.class);
        LinkRelation resourceRel = resourceMapping.getRel();

        this.isExported = annotation != null ? annotation.exported() : exposeMethodsByDefault;
        this.rel = LinkRelation
                .of(annotation == null || !StringUtils.hasText(annotation.rel()) ? method.getName() : annotation.rel());
        this.path = annotation == null || !StringUtils.hasText(annotation.path()) ? new Path(method.getName())
                : new Path(annotation.path());
        this.method = method;
        this.parameterMetadata = discoverParameterMetadata(method, resourceRel.value().concat(".").concat(rel.value()));

        List<Class<?>> parameterTypes = Arrays.asList(method.getParameterTypes());

        this.paging = parameterTypes.contains(Pageable.class);
        this.sorting = parameterTypes.contains(Sort.class);
        this.metadata = metadata;
    }

    private static final List<ParameterMetadata> discoverParameterMetadata(Method method, String baseRel) {

        List<ParameterMetadata> result = new ArrayList<ParameterMetadata>();

        for (MethodParameter parameter : new MethodParameters(method, PARAM_VALUE).getParameters()) {
            if (!IMPLICIT_PARAMETER_TYPES.contains(parameter.getParameterType())
                    && StringUtils.hasText(parameter.getParameterName())) {
                result.add(new ParameterMetadata(parameter, baseRel));
            }
        }

        return Collections.unmodifiableList(result);
    }


    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.ResourceMapping#isExported()
     */
    @Override
    public boolean isExported() {
        return isExported;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.ResourceMapping#getRel()
     */
    @Override
    public LinkRelation getRel() {
        return rel;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.ResourceMapping#getPath()
     */
    @Override
    public Path getPath() {
        return path;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.MethodResourceMapping#getMethod()
     */
    @Override
    public Method getMethod() {
        return method;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.MethodResourceMapping#getParameterMetadata()
     */
    @Override
    public ParametersMetadata getParametersMetadata() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.ResourceMapping#isPagingResource()
     */
    @Override
    public boolean isPagingResource() {
        return paging;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.MethodResourceMapping#isSortableResource()
     */
    @Override
    public boolean isSortableResource() {
        return sorting;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.ResourceMapping#getDescription()
     */
    @Override
    public ResourceDescription getDescription() {
        return null;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.rest.core.mapping.MethodResourceMapping#getProjectionSourceType()
     */
    @Override
    public Class<?> getReturnedDomainType() {
        return metadata.getReturnedDomainClass(method);
    }

}
