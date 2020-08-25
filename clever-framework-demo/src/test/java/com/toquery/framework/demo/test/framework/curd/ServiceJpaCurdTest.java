package com.toquery.framework.demo.test.framework.curd;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.entity.BizJpaNews;
import com.toquery.framework.demo.service.IBizJpaNewsService;
import com.toquery.framework.demo.test.BaseSpringTest;
import io.github.toquery.framework.common.util.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
//@DataJpaTest
//@BootstrapWith
public class ServiceJpaCurdTest extends BaseSpringTest {

    @Resource
    private IBizJpaNewsService jpaDemoService;

    @Test
    public void curd() {
        BizJpaNews save = jpaDemoService.save(new BizJpaNews("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JacksonUtils.object2String(save));

        List<BizJpaNews> saveAll = jpaDemoService.save(Lists.newArrayList(
                new BizJpaNews("saveAll-test", new Date()),
                new BizJpaNews("saveAll-test", new Date())
        ));
        log.info("插入的数据 saveBatch ：\n{}", JacksonUtils.object2String(saveAll));

        BizJpaNews updateEntity = jpaDemoService.update(save.getId(), "update-entity");
        log.info("插入的数据 updateEntity ：\n{}", JacksonUtils.object2String(updateEntity));

        save.setName("save-test-update");
        BizJpaNews update = jpaDemoService.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JacksonUtils.object2String(update));

        BizJpaNews getOne = jpaDemoService.getById(save.getId());
        log.info("查询的数据 getOne ：\n{}", JacksonUtils.object2String(getOne));

        jpaDemoService.deleteById(save.getId());
        log.info("查询的数据 deleteById ：\n{}", JacksonUtils.object2String(save));

        BizJpaNews getByName = jpaDemoService.getByName("123");
        log.info("查询的数据 getByName ：\n{}", JacksonUtils.object2String(getByName));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<BizJpaNews> updateList = jpaDemoService.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));

        List<BizJpaNews> findAll = jpaDemoService.find(null);
        log.info("查询的数据 findAll ：\n{}", JacksonUtils.object2String(findAll));

        Map<String, Object> filterParams = Maps.newHashMap();
        filterParams.put("name", "saveAll-test-update");
        List<BizJpaNews> findByFilter = jpaDemoService.find(filterParams);
        log.info("查询的数据 findByFilter: {}", JacksonUtils.object2String(filterParams));
        log.info("查询的数据 findByFilter: {}", JacksonUtils.object2String(findByFilter));


        jpaDemoService.deleteByIds(findAll.stream().map(BizJpaNews::getId).collect(Collectors.toList()));
        log.info("查询的数据 deleteById ：\n{}", JacksonUtils.object2String(save));

    }


    @Test
    public void deleteByParam() {
        List<BizJpaNews> saveAll = jpaDemoService.save(Lists.newArrayList(
                new BizJpaNews("delete-param-test", new Date()),
                new BizJpaNews("delete-param-test", new Date())
        ));

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "delete-param-test");

        log.info("删除前查询到数据为 {}", JacksonUtils.object2String(jpaDemoService.find(map)));
        jpaDemoService.delete(map);
        log.info("删除后查询到数据为 {}", JacksonUtils.object2String(jpaDemoService.find(map)));

    }

}
