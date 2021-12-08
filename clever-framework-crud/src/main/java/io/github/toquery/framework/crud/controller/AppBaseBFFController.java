package io.github.toquery.framework.crud.controller;

import io.github.toquery.framework.crud.service.impl.AppBFFServiceImpl;
import io.github.toquery.framework.webmvc.controller.AppBaseWebMvcController;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 */
public class AppBaseBFFController<BS extends AppBFFServiceImpl> extends AppBaseWebMvcController {
    protected BS bffService;

    public AppBaseBFFController(BS bffService) {
        this.bffService = bffService;
    }
}
