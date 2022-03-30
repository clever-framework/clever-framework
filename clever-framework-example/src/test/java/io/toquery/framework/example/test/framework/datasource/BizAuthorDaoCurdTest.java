package io.toquery.framework.example.test.framework.datasource;


import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.toquery.framework.example.bff.admin.author.info.dao.BizAuthorDao;
import com.toquery.framework.example.bff.admin.author.info.dao.BizAuthorMapper;
import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import com.toquery.framework.example.modules.author.info.repository.BizAuthorRepository;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.toquery.framework.example.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class BizAuthorDaoCurdTest extends BaseSpringTest {

    @Resource
    private BizAuthorRepository bizAuthorRepository;


    @Resource
    private BizAuthorDao bizAuthorDao;

    @Resource
    private BizAuthorMapper bizAuthorMapper;

    @Test
    public void testSingleCurd() {

        this.printLine();

        BizAuthor save = bizAuthorRepository.save(new BizAuthor("save-test"));
        log.info("插入的数据 save ：\n{}", JacksonUtils.object2String(save));
        Assert.assertNotNull(save);
        Assert.assertNotNull(save.getId());

        BizAuthor saveAndFlush = bizAuthorRepository.saveAndFlush(new BizAuthor("saveAndFlush-test"));
        log.info("插入的数据 saveAndFlush ：\n{}", JacksonUtils.object2String(saveAndFlush));
        Assert.assertNotNull(saveAndFlush);
        Assert.assertNotNull(saveAndFlush.getId());

        BizAuthor BizAuthor = new BizAuthor("saveAndFlush-test");
        BizAuthor.preInsert();
        BizAuthor.setCreateDateTime(LocalDateTime.now());
        BizAuthor.setUpdateDateTime(LocalDateTime.now());
//        BizAuthor saveMyBatis =
        bizAuthorMapper.saveMyBatis(BizAuthor);
        log.info("插入的数据 saveMyBatis ：\n{}", JacksonUtils.object2String(BizAuthor));
        Assert.assertNotNull(BizAuthor);
        Assert.assertNotNull(BizAuthor.getId());

        List<BizAuthor> saveAll = bizAuthorRepository.saveAll(Lists.newArrayList(
                new BizAuthor("saveAll-test-1"),
                new BizAuthor("saveAll-test-2")
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        this.printLine();

        // saveAndFlush.setName("secede update");
        saveAndFlush = bizAuthorRepository.saveAndFlush(saveAndFlush);
        log.info("插入的数据 saveAndFlush ：\n{}", JacksonUtils.object2String(saveAndFlush));

        //save.setName("save-test-update");
        BizAuthor update = bizAuthorRepository.update(save, Sets.newHashSet("authorName"));
        log.info("修改的数据 update ：\n{}", JacksonUtils.object2String(update));

        List<BizAuthor> updateAll = bizAuthorRepository.update(saveAll, Sets.newHashSet("authorName"));
        log.info("修改的数据 updateAll ：\n{}", JacksonUtils.object2String(updateAll));

        BizAuthor getOne = bizAuthorRepository.getById(save.getId());
        log.info("查询的数据 getOne ：\n{}", JacksonUtils.object2String(getOne));

        //BizAuthor getByName = bizAuthorRepository.getByName("save-test");
        //log.info("查询的数据 getByName ：\n{}", JacksonUtils.object2String(getByName));

        bizAuthorRepository.deleteById(save.getId());
        log.info("要删除的数据 deleteById ：\n{}", JacksonUtils.object2String(save));
    }

    private void printLine() {
        log.info("-----------------------------------------------------------------------------------------------");
    }

    @Test
    public void testBatchCurd() {
        List<BizAuthor> saveAll = bizAuthorRepository.saveAll(Lists.newArrayList(
                new BizAuthor("saveAll-test-1"),
                new BizAuthor("saveAll-test-2")
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        saveAll.forEach(item -> item.setAuthorName("saveAll-test-update"));
        List<BizAuthor> updateList = bizAuthorRepository.update(saveAll, Sets.newHashSet("authorName"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));

        List<BizAuthor> findAll = bizAuthorRepository.findAll();
        log.info("查询的数据 findAll ：\n{}", JacksonUtils.object2String(findAll));

        bizAuthorRepository.deleteByIds(updateList.stream().map(BizAuthor::getId).collect(Collectors.toSet()));
        log.info("要删除的数据 deleteByIds ：\n{}", JacksonUtils.object2String(updateList.stream().map(BizAuthor::getId).collect(Collectors.toSet())));
    }

    @Test
    public void testBatchCurdWithId() {
        BizAuthor BizAuthor = new BizAuthor("testBatchCurdWithId-1");
        BizAuthor.preInsert();
        List<BizAuthor> saveAll = bizAuthorRepository.saveAll(Lists.newArrayList(
                BizAuthor,
                new BizAuthor("testBatchCurdWithId-2")
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        List<BizAuthor> allNews = bizAuthorRepository.findAll();
        log.info("数据库的数据 allNews ：\n{}", JacksonUtils.object2String(allNews));

    }

    @Test
    public void testUpdate() {
        //BizAuthor tbJpaDemo = bizAuthorRepository.getByName("sss");
        //log.info("查询到的数据 getByName ：\n{}", JacksonUtils.object2String(tbJpaDemo));
    }

    @Test
    public void testUpdates() {
        List<BizAuthor> saveAll = bizAuthorRepository.saveAll(Lists.newArrayList(
                new BizAuthor("saveAll-test-1"),
                new BizAuthor("saveAll-test-2")
        ));
        log.info("插入的数据 saveAll ：\n{}", JacksonUtils.object2String(saveAll));

        saveAll.forEach(item -> item.setAuthorName("saveAll-test-update"));
        List<BizAuthor> updateList = bizAuthorRepository.update(saveAll, Sets.newHashSet("authorName"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));
    }


}
