package io.github.toquery.framework.demo.dao;

import io.github.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.repository.AppJPABaseDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author toquery
 * @version 1
 */
@Repository
public interface IJpaDemoDao extends JpaRepository<TbJpaDemo, Long> {
//public interface IJpaDemoDao extends AppJPABaseDao<TbJpaDemo, Long> {

    public TbJpaDemo getByName(String name);
}
