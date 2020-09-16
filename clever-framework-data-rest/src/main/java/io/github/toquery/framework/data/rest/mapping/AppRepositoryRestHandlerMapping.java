package io.github.toquery.framework.data.rest.mapping;

import org.springframework.data.mapping.PersistentEntity;
import org.springframework.data.mapping.context.PersistentEntities;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.support.Repositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.CollectionResourceMapping;
import org.springframework.data.rest.core.mapping.MethodResourceMapping;
import org.springframework.data.rest.core.mapping.PersistentEntitiesResourceMappings;
import org.springframework.data.rest.core.mapping.RepositoryDetectionStrategy;
import org.springframework.data.rest.core.mapping.ResourceMetadata;
import org.springframework.data.rest.core.mapping.SearchResourceMappings;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see org.springframework.data.rest.core.mapping.RepositoryResourceMappings
 */
public class AppRepositoryRestHandlerMapping extends PersistentEntitiesResourceMappings {

    private final Repositories repositories;
    private final RepositoryRestConfiguration configuration;
    private final Map<Class<?>, SearchResourceMappings> searchCache = new HashMap<Class<?>, SearchResourceMappings>();

    /**
     * Creates a new {@link PersistentEntitiesResourceMappings} from the given {@link PersistentEntities}.
     *
     * @param entities must not be {@literal null}.
     */
    public AppRepositoryRestHandlerMapping(Repositories repositories, PersistentEntities entities,
                                           RepositoryRestConfiguration configuration) {

            super(entities);

            Assert.notNull(repositories, "Repositories must not be null!");
            Assert.notNull(configuration, "RepositoryRestConfiguration must not be null!");

            this.repositories = repositories;
            this.configuration = configuration;
            this.populateCache(repositories, configuration);
    }

    @Override
    public SearchResourceMappings getSearchResourceMappings(Class<?> domainType) {

        Assert.notNull(domainType, "Type must not be null!");

        if (searchCache.containsKey(domainType)) {
            return searchCache.get(domainType);
        }

        RepositoryInformation repositoryInformation = repositories.getRequiredRepositoryInformation(domainType);
        List<MethodResourceMapping> mappings = new ArrayList<MethodResourceMapping>();
        ResourceMetadata resourceMapping = getMetadataFor(domainType);

        if (resourceMapping.isExported()) {
            for (Method queryMethod : repositoryInformation.getQueryMethods()) {
                AppEntityMethodResourceMapping methodMapping = new AppEntityMethodResourceMapping(queryMethod,
                        resourceMapping, repositoryInformation, exposeMethodsByDefault());
                if (methodMapping.isExported()) {
                    mappings.add(methodMapping);
                }
            }
        }

        SearchResourceMappings searchResourceMappings = new SearchResourceMappings(mappings);
        searchCache.put(domainType, searchResourceMappings);
        return searchResourceMappings;
    }

    private void populateCache(Repositories repositories, RepositoryRestConfiguration configuration) {

        for (Class<?> type : repositories) {

            RepositoryInformation repositoryInformation = repositories.getRequiredRepositoryInformation(type);
            Class<?> repositoryInterface = repositoryInformation.getRepositoryInterface();
            PersistentEntity<?, ?> entity = repositories.getPersistentEntity(type);

            RepositoryDetectionStrategy strategy = configuration.getRepositoryDetectionStrategy();
            LinkRelationProvider provider = configuration.getRelProvider();

            CollectionResourceMapping mapping = new AppEntityRestCollectionResourceMapping(repositoryInformation, strategy,
                    provider);
//            RepositoryAwareResourceMetadata information = new RepositoryAwareResourceMetadata(entity, mapping, this,
//                    repositoryInformation);

        }
    }

    boolean exposeMethodsByDefault() {
        return configuration.exposeRepositoryMethodsByDefault();
    }
}
