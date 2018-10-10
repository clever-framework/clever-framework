package io.github.toquery.framework.demo.dao;

import io.github.toquery.framework.demo.entity.TbJpaDemoLong;
import io.github.toquery.framework.repository.AppJPABaseDao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author toquery
 * @version 1
 */
//@Repository
//public interface IJpaDemoDao extends JpaRepository<TbJpaDemoLong, Long> {
public interface IJpaDemoDao extends AppJPABaseDao<TbJpaDemoLong, Long> {

    public TbJpaDemoLong getByName(String name);
}
