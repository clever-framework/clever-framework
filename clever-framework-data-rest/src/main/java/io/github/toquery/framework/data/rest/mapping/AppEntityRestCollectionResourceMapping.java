package io.github.toquery.framework.data.rest.mapping;

import io.github.toquery.framework.data.rest.annotation.AppEntityRest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.rest.core.Path;
import org.springframework.data.rest.core.mapping.CollectionResourceMapping;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.core.mapping.ResourceDescription;
import org.springframework.data.util.Optionals;
import org.springframework.hateoas.LinkRelation;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * org.springframework.data.rest.core.mapping.RepositoryCollectionResourceMapping
 */
public class AppEntityRestCollectionResourceMapping implements CollectionResourceMapping {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppEntityRestCollectionResourceMapping.class);
    private static final boolean EVO_INFLECTOR_IS_PRESENT = ClassUtils.isPresent("org.atteo.evo.inflector.English", null);

    private final boolean repositoryExported;
    private final RepositoryMetadata metadata;
    private final Path path;
//
//    private final Lazy<LinkRelation> rel, itemResourceRel;
//    private final Lazy<ResourceDescription> description, itemDescription;
//    private final Lazy<Class<?>> excerptProjection;

    public AppEntityRestCollectionResourceMapping(RepositoryMetadata metadata, RepositoryDetectionStrategy strategy) {
        this(metadata, strategy, new EvoInflectorLinkRelationProvider());
    }


    /**
     */
    AppEntityRestCollectionResourceMapping(RepositoryMetadata metadata, RepositoryDetectionStrategy strategy,
                                        LinkRelationProvider relProvider) {

        Assert.notNull(metadata, "Repository metadata must not be null!");
        Assert.notNull(relProvider, "LinkRelationProvider must not be null!");
        Assert.notNull(strategy, "RepositoryDetectionStrategy must not be null!");

//        Class<?> repositoryType = metadata.getRepositoryInterface();
        Class<?> domainType = metadata.getDomainType();
        CollectionResourceMapping domainTypeMapping = new TypeBasedCollectionResourceMapping(domainType, relProvider);


        Optional<AppEntityRest> annotation = Optional
                .ofNullable(AnnotationUtils.findAnnotation(metadata.getDomainType(), AppEntityRest.class));

        this.metadata = metadata;
        this.repositoryExported = strategy.isExported(metadata);



        this.path = Optionals.firstNonEmpty(//
                () -> annotation.map(AppEntityRest::path)) //
                .filter(StringUtils::hasText) //
                .filter(it -> {
                    if (it.contains("/")) {
                        throw new IllegalStateException(
                                String.format("Path %s configured for %s must only contain a single path segment!", it, metadata.getRepositoryInterface().getName()));
                    }
                    return true;
                })
                // .peek(it -> it.wait()) //
                .map(Path::new)//
                .orElseGet(domainTypeMapping::getPath);

    }

    @Override
    public LinkRelation getItemResourceRel() {
        return null;
    }

    @Override
    public ResourceDescription getItemResourceDescription() {
        return null;
    }

    @Override
    public Optional<Class<?>> getExcerptProjection() {
        return Optional.empty();
    }

    @Override
    public boolean isExported() {
        return false;
    }

    @Override
    public LinkRelation getRel() {
        return null;
    }

    @Override
    public Path getPath() {
        return path;
    }

    @Override
    public boolean isPagingResource() {
        return false;
    }

    @Override
    public ResourceDescription getDescription() {
        return null;
    }
}
