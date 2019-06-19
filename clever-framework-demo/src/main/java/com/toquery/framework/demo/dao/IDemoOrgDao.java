package com.toquery.framework.demo.dao;

import com.toquery.framework.demo.entity.DemoOrg;
import com.toquery.framework.demo.entity.DemoUser;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;

/**
 * @author toquery
 * @version 1
 */

public interface IDemoOrgDao extends AppJpaBaseRepository<DemoOrg, Long> {

//    @SuppressWarnings("MybatisMapperMethodInspection")

}
