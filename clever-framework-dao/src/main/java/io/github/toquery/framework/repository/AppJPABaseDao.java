package io.github.toquery.framework.repository;

import io.github.toquery.framework.repository.AppJpaDataRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AppJPABaseDao<T,ID extends Serializable> extends AppJpaDataRepository<T, ID> {
}
