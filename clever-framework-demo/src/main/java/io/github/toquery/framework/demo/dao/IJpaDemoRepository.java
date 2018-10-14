package io.github.toquery.framework.demo.dao;

import io.github.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;

/**
 * @author toquery
 * @version 1
 */
//@Repository
//public interface IJpaDemoRepository extends JpaRepository<TbJpaDemo, Long> {
public interface IJpaDemoRepository extends AppJpaBaseRepository<TbJpaDemo, Long> {

    public TbJpaDemo getByName(String name);
}
