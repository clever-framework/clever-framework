package com.toquery.framework.demo.test.curd;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IMyBatisDemoDao;
import com.toquery.framework.demo.entity.TbMyBatisDemo;
import com.toquery.framework.demo.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class DaoMybatisCurdTest extends BaseSpringTest {

    @Resource
    private IMyBatisDemoDao myBatisDemoDao;

    @Test
    public void testSingleCurd() {
        TbMyBatisDemo save = myBatisDemoDao.save(new TbMyBatisDemo("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JSON.toJSONString(save));

        TbMyBatisDemo saveAndFlush = myBatisDemoDao.saveAndFlush(new TbMyBatisDemo("saveAndFlush-test", new Date()));
        log.info("插入的数据 saveAndFlush ：\n{}", JSON.toJSONString(saveAndFlush));

        save.setName("save-test-update");
        TbMyBatisDemo update = myBatisDemoDao.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JSON.toJSONString(update));

        TbMyBatisDemo getOne = myBatisDemoDao.getOne(save.getId());
        log.info("查询的数据 getOne ：\n{}", JSON.toJSONString(getOne));

        TbMyBatisDemo getByName = myBatisDemoDao.getByMyBatisName("save-test-update");
        log.info("查询的数据 getByName ：\n{}", JSON.toJSONString(getByName));

        TbMyBatisDemo getByName2 = myBatisDemoDao.getByName2("save-test-update");
        log.info("查询的数据 getByName2 ：\n{}", JSON.toJSONString(getByName2));

        myBatisDemoDao.deleteById(save.getId());
        log.info("根据ID数据 deleteById ：\n{}", JSON.toJSONString(save));
    }

    @Test
    public void testBatchCurd() {
        List<TbMyBatisDemo> saveAll = myBatisDemoDao.saveAll(Lists.newArrayList(
                new TbMyBatisDemo("saveAll-test-1", new Date()),
                new TbMyBatisDemo("saveAll-test-2", new Date())
        ));
        log.info("批量插入的数据 saveAll ：\n{}", JSON.toJSONString(saveAll));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<TbMyBatisDemo> updateList = myBatisDemoDao.update(saveAll, Sets.newHashSet("name"));
        log.info("批量修改的数据 updateList ：\n{}", JSON.toJSONString(updateList));

        List<TbMyBatisDemo> findAll = myBatisDemoDao.findAll();
        log.info("批量查询的数据 findAll ：\n{}", JSON.toJSONString(findAll));

        Set<Long> needDeleteIds = updateList.stream().map(TbMyBatisDemo::getId).collect(Collectors.toSet());
        myBatisDemoDao.deleteByIds(needDeleteIds);
        log.info("批量数据根据ID deleteByIds ：\n{}", JSON.toJSONString(needDeleteIds));
    }

    @Test
    public void getByName(){
        TbMyBatisDemo tbMyBatisDemo = myBatisDemoDao.getByMyBatisName("111");
        log.info("通过Mybatis的数据 getByName ：\n{}", JSON.toJSONString(tbMyBatisDemo));
    }
}
