package com.toquery.framework.demo.test.curd;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IBizBatisNewsMapper;
import com.toquery.framework.demo.entity.BizBatisNews;
import com.toquery.framework.demo.test.BaseSpringTest;
import io.github.toquery.framework.common.util.JacksonUtils;
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
    private IBizBatisNewsMapper myBatisDemoDao;

    @Test
    public void testSingleCurd() {
        BizBatisNews save = myBatisDemoDao.save(new BizBatisNews("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JacksonUtils.object2String(save));

        BizBatisNews saveAndFlush = myBatisDemoDao.saveAndFlush(new BizBatisNews("saveAndFlush-test", new Date()));
        log.info("插入的数据 saveAndFlush ：\n{}", JacksonUtils.object2String(saveAndFlush));

        save.setName("save-test-update");
        BizBatisNews update = myBatisDemoDao.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JacksonUtils.object2String(update));

        BizBatisNews getOne = myBatisDemoDao.getOne(save.getId());
        log.info("查询的数据 getOne ：\n{}", JacksonUtils.object2String(getOne));

        BizBatisNews getByName = myBatisDemoDao.getByMyBatisName("save-test-update");
        log.info("查询的数据 getByName ：\n{}", JacksonUtils.object2String(getByName));

        BizBatisNews getByName2 = myBatisDemoDao.getByName2("save-test-update");
        log.info("查询的数据 getByName2 ：\n{}", JacksonUtils.object2String(getByName2));

        myBatisDemoDao.deleteById(save.getId());
        log.info("根据ID数据 deleteById ：\n{}", JacksonUtils.object2String(save));
    }

    @Test
    public void testBatchCurd() {
        List<BizBatisNews> saveAll = myBatisDemoDao.saveAll(Lists.newArrayList(
                new BizBatisNews("saveAll-test-1", new Date()),
                new BizBatisNews("saveAll-test-2", new Date())
        ));
        log.info("批量插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<BizBatisNews> updateList = myBatisDemoDao.update(saveAll, Sets.newHashSet("name"));
        log.info("批量修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));

        List<BizBatisNews> findAll = myBatisDemoDao.findAll();
        log.info("批量查询的数据 findAll ：\n{}", JacksonUtils.object2String(findAll));

        Set<Long> needDeleteIds = updateList.stream().map(BizBatisNews::getId).collect(Collectors.toSet());
        myBatisDemoDao.deleteByIds(needDeleteIds);
        log.info("批量数据根据ID deleteByIds ：\n{}", JacksonUtils.object2String(needDeleteIds));
    }

    @Test
    public void getByName(){
        BizBatisNews tbMyBatisDemo = myBatisDemoDao.getByMyBatisName("111");
        log.info("通过Mybatis的数据 getByName ：\n{}", JacksonUtils.object2String(tbMyBatisDemo));
    }
}
