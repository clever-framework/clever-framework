package io.github.toquery.framework.demo.test.curd;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.demo.dao.IJpaDemoRepository;
import io.github.toquery.framework.demo.entity.TbJpaDemo;
import io.github.toquery.framework.demo.service.IMyBatisDemoService;
import io.github.toquery.framework.demo.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class DaoCurdTest extends BaseSpringTest {

    @Resource
    private IJpaDemoRepository jpaDemoRepository;
    @Resource
    private IMyBatisDemoService myBatisDemoService;

    @Test
    public void curd() {
        TbJpaDemo save = jpaDemoRepository.save(new TbJpaDemo("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JSON.toJSONString(save));

        TbJpaDemo saveAndFlush = jpaDemoRepository.saveAndFlush(new TbJpaDemo("saveAndFlush-test", new Date()));
        log.info("插入的数据 saveAndFlush ：\n{}", JSON.toJSONString(saveAndFlush));

        List<TbJpaDemo> saveAll = jpaDemoRepository.saveAll(Lists.newArrayList(
                new TbJpaDemo("saveAll-test", new Date()),
                new TbJpaDemo("saveAll-test", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JSON.toJSONString(saveAll));


        save.setName("save-test-update");
        TbJpaDemo update = jpaDemoRepository.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JSON.toJSONString(update));

        saveAll.forEach(item -> item.setName("saveAll-test-update"));
        List<TbJpaDemo> updateList = jpaDemoRepository.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JSON.toJSONString(updateList));

        TbJpaDemo getOne = jpaDemoRepository.getOne(save.getId());
        log.info("查询的数据 getOne ：\n{}", JSON.toJSONString(getOne));

        TbJpaDemo getByName = jpaDemoRepository.getByName("123");
        log.info("查询的数据 getByName ：\n{}", JSON.toJSONString(getByName));

        List<TbJpaDemo> findAll = jpaDemoRepository.findAll();
        log.info("查询的数据 findAll ：\n{}", JSON.toJSONString(findAll));

        jpaDemoRepository.deleteById(save.getId());
        log.info("查询的数据 deleteById ：\n{}", JSON.toJSONString(save));

    }
}
