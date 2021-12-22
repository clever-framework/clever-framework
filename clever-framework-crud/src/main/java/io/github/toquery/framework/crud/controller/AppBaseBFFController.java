package io.github.toquery.framework.crud.controller;

import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;

/**
 * @param <E>  实体类
 * @param <BS> bff service
 * @param <BD> bff dao
 */
public class AppBaseBFFController<E extends AppBaseEntity, BS extends AppBFFServiceImpl<E, BD>, BD extends AppJpaBaseRepository<E>> extends AppBaseWebMvcController {
    protected BS bffService;

    public AppBaseBFFController(BS bffService) {
        this.bffService = bffService;
    }
}
