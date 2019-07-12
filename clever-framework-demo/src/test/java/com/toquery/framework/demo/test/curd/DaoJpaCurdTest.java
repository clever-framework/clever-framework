package com.toquery.framework.demo.test.curd;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IBizJpaNewsRepository;
import com.toquery.framework.demo.entity.BizJpaNews;
import com.toquery.framework.demo.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class DaoJpaCurdTest extends BaseSpringTest {

    @Resource
    private IBizJpaNewsRepository jpaDemoRepository;


    @Test
    public void testSingleCurd() {
        BizJpaNews save = jpaDemoRepository.save(new BizJpaNews("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JSON.toJSONString(save));

        BizJpaNews saveAndFlush = jpaDemoRepository.saveAndFlush(new BizJpaNews("saveAndFlush-test", new Date()));
        log.info("插入的数据 saveAndFlush ：\n{}", JSON.toJSONString(saveAndFlush));


        saveAndFlush.setName("secede update");
        saveAndFlush = jpaDemoRepository.saveAndFlush(saveAndFlush);
        log.info("插入的数据 saveAndFlush ：\n{}", JSON.toJSONString(saveAndFlush));

        save.setName("save-test-update");
        BizJpaNews update = jpaDemoRepository.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JSON.toJSONString(update));

        BizJpaNews getOne = jpaDemoRepository.getOne(save.getId());
        log.info("查询的数据 getOne ：\n{}", JSON.toJSONString(getOne));

        BizJpaNews getByName = jpaDemoRepository.getByName("save-test");
        log.info("查询的数据 getByName ：\n{}", JSON.toJSONString(getByName));

        jpaDemoRepository.deleteById(save.getId());
        log.info("要删除的数据 deleteById ：\n{}", JSON.toJSONString(save));
    }

    @Test
    public void testBatchCurd() {
        List<BizJpaNews> saveAll = jpaDemoRepository.saveAll(Lists.newArrayList(
                new BizJpaNews("saveAll-test-1", new Date()),
                new BizJpaNews("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JSON.toJSONString(saveAll));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<BizJpaNews> updateList = jpaDemoRepository.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JSON.toJSONString(updateList));

        List<BizJpaNews> findAll = jpaDemoRepository.findAll();
        log.info("查询的数据 findAll ：\n{}", JSON.toJSONString(findAll));

        jpaDemoRepository.deleteByIds(updateList.stream().map(BizJpaNews::getId).collect(Collectors.toSet()));
        log.info("要删除的数据 deleteByIds ：\n{}", JSON.toJSONString(updateList.stream().map(BizJpaNews::getId).collect(Collectors.toSet())));
    }

    @Test
    public void testUpdate() {
        BizJpaNews tbJpaDemo = jpaDemoRepository.getByName("sss");
        log.info("查询到的数据 getByName ：\n{}", JSON.toJSONString(tbJpaDemo));
    }

    @Test
    public void testUpdates() {
        List<BizJpaNews> saveAll = jpaDemoRepository.saveAll(Lists.newArrayList(
                new BizJpaNews("saveAll-test-1", new Date()),
                new BizJpaNews("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JSON.toJSONString(saveAll));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<BizJpaNews> updateList = jpaDemoRepository.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JSON.toJSONString(updateList));
    }


}
