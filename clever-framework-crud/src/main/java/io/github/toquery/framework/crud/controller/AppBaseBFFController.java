package io.github.toquery.framework.crud.controller;

import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
import io.github.toquery.framework.core.entity.BaseEntity;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;

/**
 * @param <E>  实体类
 * @param <BS> bff service
 * @param <BD> bff dao
 */
public class AppBaseBFFController<E extends BaseEntity, BS extends AppBFFServiceImpl<E, BD>, BD extends AppJpaBaseRepository<E>> extends AppBaseWebMvcController {
    protected BS bffService;

    public AppBaseBFFController(BS bffService) {
        this.bffService = bffService;
    }
}
