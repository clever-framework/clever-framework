package com.toquery.framework.demo.service;


import com.toquery.framework.demo.entity.DemoUser;
import com.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.curd.service.AppBaseService;
import org.springframework.data.domain.Page;

/**
 * @author toquery
 * @version 1
 */

public interface IDemoUserService extends AppBaseService<DemoUser, Long> {

}
