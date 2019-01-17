package com.toquery.framework.demo.test.curd;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.toquery.framework.demo.entity.TbJpaDemo;
import com.toquery.framework.demo.service.IJpaDemoService;
import com.toquery.framework.demo.test.BaseSpringTest;
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
public class ServiceJpaCurdTest extends BaseSpringTest {

    @Resource
    private IJpaDemoService jpaDemoService;

    @Test
    public void curd() {
        TbJpaDemo save = jpaDemoService.save(new TbJpaDemo("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JSON.toJSONString(save));

        List<TbJpaDemo> saveAll = jpaDemoService.save(Lists.newArrayList(
                new TbJpaDemo("saveAll-test", new Date()),
                new TbJpaDemo("saveAll-test", new Date())
        ));
        log.info("插入的数据 saveBatch ：\n{}", JSON.toJSONString(saveAll));

        TbJpaDemo updateEntity = jpaDemoService.update(save.getId(), "update-entity");
        log.info("插入的数据 updateEntity ：\n{}", JSON.toJSONString(updateEntity));

        save.setName("save-test-update");
        TbJpaDemo update = jpaDemoService.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JSON.toJSONString(update));

        TbJpaDemo getOne = jpaDemoService.getById(save.getId());
        log.info("查询的数据 getOne ：\n{}", JSON.toJSONString(getOne));

        jpaDemoService.deleteById(save.getId());
        log.info("查询的数据 deleteById ：\n{}", JSON.toJSONString(save));

        TbJpaDemo getByName = jpaDemoService.getByName("123");
        log.info("查询的数据 getByName ：\n{}", JSON.toJSONString(getByName));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<TbJpaDemo> updateList = jpaDemoService.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JSON.toJSONString(updateList));

        List<TbJpaDemo> findAll = jpaDemoService.find(null);
        log.info("查询的数据 findAll ：\n{}", JSON.toJSONString(findAll));

        jpaDemoService.deleteByIds(findAll.stream().map(TbJpaDemo::getId).collect(Collectors.toList()));
        log.info("查询的数据 deleteById ：\n{}", JSON.toJSONString(save));

    }



    @Test
    public void deleteByParam() {
        List<TbJpaDemo> saveAll = jpaDemoService.save(Lists.newArrayList(
                new TbJpaDemo("delete-param-test", new Date()),
                new TbJpaDemo("delete-param-test", new Date())
        ));

        Map<String,Object> map = Maps.newHashMap();
        map.put("name","delete-param-test");

        log.info("删除前查询到数据为 {}",JSON.toJSONString(jpaDemoService.find(map)));
        jpaDemoService.delete(map);
        log.info("删除后查询到数据为 {}",JSON.toJSONString(jpaDemoService.find(map)));

    }

}
