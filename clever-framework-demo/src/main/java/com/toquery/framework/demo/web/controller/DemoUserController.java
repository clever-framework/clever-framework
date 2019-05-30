package com.toquery.framework.demo.web.controller;

import com.toquery.framework.demo.entity.DemoUser;
import com.toquery.framework.demo.service.IDemoUserService;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/demo/user")
public class DemoUserController extends AppBaseCurdController<IDemoUserService, DemoUser, Long> {
}
