package com.toquery.framework.demo.web.controller;

import com.toquery.framework.demo.entity.DemoOrg;
import com.toquery.framework.demo.service.IDemoOrgService;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author toquery
 * @version 1
 */

@RequestMapping("/demo/org")
public class DemoOrgController extends AppBaseCurdController<IDemoOrgService, DemoOrg, Long> {
}
