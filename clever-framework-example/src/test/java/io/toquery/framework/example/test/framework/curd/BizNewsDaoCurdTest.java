package io.toquery.framework.example.test.framework.curd;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.toquery.framework.example.dao.IBizNewsRepository;
import com.toquery.framework.example.entity.BizNews;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.dao.primary.snowflake.SnowFlake;
import io.toquery.framework.example.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class BizNewsDaoCurdTest extends BaseSpringTest {

    @Resource
    private IBizNewsRepository bizNewsRepository;


    @Test
    public void testSingleCurd() {

        this.printLine();

        BizNews save = bizNewsRepository.save(new BizNews("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JacksonUtils.object2String(save));
        Assert.assertNotNull(save);
        Assert.assertNotNull(save.getId());

        BizNews saveAndFlush = bizNewsRepository.saveAndFlush(new BizNews("saveAndFlush-test", new Date()));
        log.info("插入的数据 saveAndFlush ：\n{}", JacksonUtils.object2String(saveAndFlush));
        Assert.assertNotNull(saveAndFlush);
        Assert.assertNotNull(saveAndFlush.getId());

        BizNews bizNews = new BizNews(new SnowFlake().nextId(), "saveAndFlush-test", new Date());
        bizNews.setCreateDateTime(LocalDateTime.now());
        bizNews.setUpdateDateTime(LocalDateTime.now());
//        BizNews saveMyBatis =
        bizNewsRepository.saveMyBatis(bizNews);
        log.info("插入的数据 saveMyBatis ：\n{}", JacksonUtils.object2String(bizNews));
        Assert.assertNotNull(bizNews);
        Assert.assertNotNull(bizNews.getId());

        List<BizNews> saveAll = bizNewsRepository.saveAll(Lists.newArrayList(
                new BizNews("saveAll-test-1", new Date()),
                new BizNews("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        this.printLine();

        // saveAndFlush.setName("secede update");
        saveAndFlush = bizNewsRepository.saveAndFlush(saveAndFlush);
        log.info("插入的数据 saveAndFlush ：\n{}", JacksonUtils.object2String(saveAndFlush));

        //save.setName("save-test-update");
        BizNews update = bizNewsRepository.update(save, Sets.newHashSet("title"));
        log.info("修改的数据 update ：\n{}", JacksonUtils.object2String(update));

        List<BizNews> updateAll = bizNewsRepository.update(saveAll, Sets.newHashSet("title"));
        log.info("修改的数据 updateAll ：\n{}", JacksonUtils.object2String(updateAll));

        BizNews getOne = bizNewsRepository.getById(save.getId());
        log.info("查询的数据 getOne ：\n{}", JacksonUtils.object2String(getOne));

        //BizNews getByName = bizNewsRepository.getByName("save-test");
        //log.info("查询的数据 getByName ：\n{}", JacksonUtils.object2String(getByName));

        bizNewsRepository.deleteById(save.getId());
        log.info("要删除的数据 deleteById ：\n{}", JacksonUtils.object2String(save));
    }

    private void printLine() {
        log.info("-----------------------------------------------------------------------------------------------");
    }

    @Test
    public void testBatchCurd() {
        List<BizNews> saveAll = bizNewsRepository.saveAll(Lists.newArrayList(
                new BizNews("saveAll-test-1", new Date()),
                new BizNews("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        saveAll.forEach(item -> item.setTitle("saveAll-test-update"));
        List<BizNews> updateList = bizNewsRepository.update(saveAll, Sets.newHashSet("title"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));

        List<BizNews> findAll = bizNewsRepository.findAll();
        log.info("查询的数据 findAll ：\n{}", JacksonUtils.object2String(findAll));

        bizNewsRepository.deleteByIds(updateList.stream().map(BizNews::getId).collect(Collectors.toSet()));
        log.info("要删除的数据 deleteByIds ：\n{}", JacksonUtils.object2String(updateList.stream().map(BizNews::getId).collect(Collectors.toSet())));
    }

    @Test
    public void testBatchCurdWithId() {
        BizNews bizNews = new BizNews(101L, "testBatchCurdWithId-1", new Date());
        bizNews.preInsert();
        List<BizNews> saveAll = bizNewsRepository.saveAll(Lists.newArrayList(
                bizNews,
                new BizNews(102L, "testBatchCurdWithId-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        List<BizNews> allNews = bizNewsRepository.findAll();
        log.info("数据库的数据 allNews ：\n{}", JacksonUtils.object2String(allNews));

    }

    @Test
    public void testUpdate() {
        //BizNews tbJpaDemo = bizNewsRepository.getByName("sss");
        //log.info("查询到的数据 getByName ：\n{}", JacksonUtils.object2String(tbJpaDemo));
    }

    @Test
    public void testUpdates() {
        List<BizNews> saveAll = bizNewsRepository.saveAll(Lists.newArrayList(
                new BizNews("saveAll-test-1", new Date()),
                new BizNews("saveAll-test-2", new Date())
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        saveAll.forEach(item -> item.setTitle("saveAll-test-update"));
        List<BizNews> updateList = bizNewsRepository.update(saveAll, Sets.newHashSet("title"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));
    }


}
