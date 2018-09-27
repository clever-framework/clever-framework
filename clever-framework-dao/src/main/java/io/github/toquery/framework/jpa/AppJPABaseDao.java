package io.github.toquery.framework.jpa;

import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

@NoRepositoryBean
public interface AppJPABaseDao<T,ID extends Serializable> extends AppJpaDataRepository<T, ID> {
}
