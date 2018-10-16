package io.github.toquery.framework.demo.dao;

import io.github.toquery.framework.dao.repository.AppJpaBaseRepository;
import io.github.toquery.framework.demo.entity.TbJpaDemo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author toquery
 * @version 1
 */
//@Repository
//public interface IJpaDemoRepository extends JpaRepository<TbJpaDemo, Long> {
public interface IJpaDemoRepository extends AppJpaBaseRepository<TbJpaDemo, Long> {

    @Query("from TbJpaDemo where name = :name")
    public TbJpaDemo getByName(@Param("name") String name);
}
