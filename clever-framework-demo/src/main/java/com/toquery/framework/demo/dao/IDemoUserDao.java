package com.toquery.framework.demo.dao;

import com.toquery.framework.demo.entity.DemoUser;
import com.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * @author toquery
 * @version 1
 */

public interface IDemoUserDao extends AppJpaBaseRepository<DemoUser, Long> {

//    @SuppressWarnings("MybatisMapperMethodInspection")

}
