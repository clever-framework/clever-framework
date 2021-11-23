package com.toquery.framework.example.modules.news.service;


import com.toquery.framework.example.modules.news.entity.BizNews;
import io.github.toquery.framework.crud.service.AppBaseService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */

public interface IBizNewsService extends AppBaseService<BizNews> {

    Page<BizNews> findByName(String name, Integer page, Integer size);

    org.springframework.data.domain.Page<BizNews> queryJpaByPage(int current, int requestPageSize);

    com.github.pagehelper.Page<BizNews> queryMyBatisByPage(int current, int requestPageSize);

    List<BizNews> findJpa();

    List<BizNews> findFilter();

    List<BizNews> findMyBatis();


    BizNews getById(Long id);



    BizNews saveJpa(BizNews bizNews);

    BizNews saveMyBatis(BizNews bizNews);



    BizNews updateJpa(BizNews bizNews);

    BizNews updateMyBatis(BizNews bizNews);

    BizNews update(Long id, String name);


    void deleteJpa(Set<Long> ids);

    void deleteMyBatis(Set<Long> ids);

    BizNews detailJpa(Long id);

    BizNews detailMyBatis(Long id);
}
