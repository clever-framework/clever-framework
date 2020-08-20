package com.toquery.framework.demo.test.curd;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.dao.IBizJpaNewsRepository;
import com.toquery.framework.demo.entity.BizJpaNews;
import com.toquery.framework.demo.test.BaseSpringTest;
import io.github.toquery.framework.common.util.JacksonUtils;
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
        log.info("插入的数据 save ：\n{}", JacksonUtils.object2String(save));

        BizJpaNews saveAndFlush = jpaDemoRepository.saveAndFlush(new BizJpaNews("saveAndFlush-test", new Date()));
        log.info("插入的数据 saveAndFlush ：\n{}", JacksonUtils.object2String(saveAndFlush));


        saveAndFlush.setName("secede update");
        saveAndFlush = jpaDemoRepository.saveAndFlush(saveAndFlush);
        log.info("插入的数据 saveAndFlush ：\n{}", JacksonUtils.object2String(saveAndFlush));

        save.setName("save-test-update");
        BizJpaNews update = jpaDemoRepository.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JacksonUtils.object2String(update));

        BizJpaNews getOne = jpaDemoRepository.getOne(save.getId());
        log.info("查询的数据 getOne ：\n{}", JacksonUtils.object2String(getOne));

        BizJpaNews getByName = jpaDemoRepository.getByName("save-test");
        log.info("查询的数据 getByName ：\n{}", JacksonUtils.object2String(getByName));

        jpaDemoRepository.deleteById(save.getId());
        log.info("要删除的数据 deleteById ：\n{}", JacksonUtils.object2String(save));
    }

    @Test
    public void testBatchCurd() {
        List<BizJpaNews> saveAll = jpaDemoRepository.saveAll(Lists.newArrayList(
                new BizJpaNews("saveAll-test-1", new Date()),
                new BizJpaNews("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<BizJpaNews> updateList = jpaDemoRepository.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));

        List<BizJpaNews> findAll = jpaDemoRepository.findAll();
        log.info("查询的数据 findAll ：\n{}", JacksonUtils.object2String(findAll));

        jpaDemoRepository.deleteByIds(updateList.stream().map(BizJpaNews::getId).collect(Collectors.toSet()));
        log.info("要删除的数据 deleteByIds ：\n{}", JacksonUtils.object2String(updateList.stream().map(BizJpaNews::getId).collect(Collectors.toSet())));
    }

    @Test
    public void testUpdate() {
        BizJpaNews tbJpaDemo = jpaDemoRepository.getByName("sss");
        log.info("查询到的数据 getByName ：\n{}", JacksonUtils.object2String(tbJpaDemo));
    }

    @Test
    public void testUpdates() {
        List<BizJpaNews> saveAll = jpaDemoRepository.saveAll(Lists.newArrayList(
                new BizJpaNews("saveAll-test-1", new Date()),
                new BizJpaNews("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<BizJpaNews> updateList = jpaDemoRepository.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));
    }


}
