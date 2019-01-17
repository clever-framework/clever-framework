package com.toquery.framework.demo.service.impl;

import com.toquery.framework.demo.dao.mybatis.NewsMapper;
import com.toquery.framework.demo.entity.TbNewsDemo;
import com.toquery.framework.demo.service.INewsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Service
public class NewsServiceImpl implements INewsService {

    @Resource
    private NewsMapper newsMapper;

    @Override
    public void update(TbNewsDemo news) {
        newsMapper.update(news.getId(), news.getTitle());
    }
}
