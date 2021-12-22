package io.github.toquery.framework.crud.service.impl;

import io.github.toquery.framework.crud.service.AppBFFService;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;

import java.util.Map;

/**
 *
 */
public abstract class AppBFFServiceImpl<E extends AppBaseEntity, D extends AppJpaBaseRepository<E>> extends AppBaseServiceImpl<E, D> implements AppBFFService {

}
