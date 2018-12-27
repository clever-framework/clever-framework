package com.toquery.framework.demo.test.curd;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IJpaDemoRepository;
import com.toquery.framework.demo.entity.TbJpaDemo;
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
    private IJpaDemoRepository jpaDemoRepository;


    @Test
    public void testSingleCurd() {
        TbJpaDemo save = jpaDemoRepository.save(new TbJpaDemo("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JSON.toJSONString(save));

        TbJpaDemo saveAndFlush = jpaDemoRepository.saveAndFlush(new TbJpaDemo("saveAndFlush-test", new Date()));
        log.info("插入的数据 saveAndFlush ：\n{}", JSON.toJSONString(saveAndFlush));

        save.setName("save-test-update");
        TbJpaDemo update = jpaDemoRepository.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JSON.toJSONString(update));

        TbJpaDemo getOne = jpaDemoRepository.getOne(save.getId());
        log.info("查询的数据 getOne ：\n{}", JSON.toJSONString(getOne));

        TbJpaDemo getByName = jpaDemoRepository.getByName("save-test");
        log.info("查询的数据 getByName ：\n{}", JSON.toJSONString(getByName));

        jpaDemoRepository.deleteById(save.getId());
        log.info("要删除的数据 deleteById ：\n{}", JSON.toJSONString(save));
    }

    @Test
    public void testBatchCurd() {
        List<TbJpaDemo> saveAll = jpaDemoRepository.saveAll(Lists.newArrayList(
                new TbJpaDemo("saveAll-test-1", new Date()),
                new TbJpaDemo("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JSON.toJSONString(saveAll));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<TbJpaDemo> updateList = jpaDemoRepository.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JSON.toJSONString(updateList));

        List<TbJpaDemo> findAll = jpaDemoRepository.findAll();
        log.info("查询的数据 findAll ：\n{}", JSON.toJSONString(findAll));

        jpaDemoRepository.deleteByIds(updateList.stream().map(TbJpaDemo::getId).collect(Collectors.toSet()));
        log.info("要删除的数据 deleteByIds ：\n{}", JSON.toJSONString(updateList.stream().map(TbJpaDemo::getId).collect(Collectors.toSet())));
    }
}
