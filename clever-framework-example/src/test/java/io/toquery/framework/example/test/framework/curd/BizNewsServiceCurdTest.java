package io.toquery.framework.example.test.framework.curd;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.toquery.framework.example.bff.admin.news.info.service.BizNewsService;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.toquery.framework.example.test.BaseSpringTest;
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
public class BizNewsServiceCurdTest extends BaseSpringTest {

    @Resource
    private BizNewsService bizNewsService;


    @Resource
    private BizNewsDomainService bizNewsDomainService;

    @Test
    public void updatePart() {
        String queryType = "APP";
        BizNews bizNews = new BizNews("update-part-test", new Date());
        bizNewsDomainService.save(bizNews);

        BizNews updatePart = new BizNews();
        updatePart.setId(bizNews.getId());
        updatePart.setLikeNum(11);
        BizNews newUpdate = bizNewsDomainService.update(updatePart, Sets.newHashSet("title", "showTime", "likeNum"));

        log.info("查询的数据 newUpdate ：\n{}", JacksonUtils.object2String(bizNewsDomainService.getById(newUpdate.getId())));

    }

    @Test
    public void updatePartByJPA() {
        BizNews bizNews = new BizNews("update-part-test", new Date());
        bizNewsDomainService.save(bizNews);


        log.info("查询的数据 bizNews ：\n{}", JacksonUtils.object2String(bizNewsDomainService.getById(bizNews.getId())));

        BizNews updatePart = new BizNews();
        updatePart.setId(bizNews.getId());
        updatePart.setLikeNum(11);

        BizNews newUpdate = bizNewsService.updateJpa(updatePart);

        log.info("查询的数据 newUpdate ：\n{}", JacksonUtils.object2String(bizNewsDomainService.getById(newUpdate.getId())));
    }

    @Test
    public void curd() {
        BizNews save = bizNewsDomainService.save(new BizNews("save-test", new Date()));
        log.info("插入的数据 save ：\n{}", JacksonUtils.object2String(save));

        List<BizNews> saveAll = bizNewsDomainService.save(Lists.newArrayList(
                new BizNews("saveAll-test", new Date()),
                new BizNews("saveAll-test", new Date())
        ));
        log.info("插入的数据 saveBatch ：\n{}", JacksonUtils.object2String(saveAll));

        BizNews updateEntity = bizNewsService.update(save.getId(), "update-entity");
        log.info("插入的数据 updateEntity ：\n{}", JacksonUtils.object2String(updateEntity));

        save.setTitle("save-test-update");
        BizNews update = bizNewsDomainService.update(save, Sets.newHashSet("name"));
        log.info("修改的数据 update ：\n{}", JacksonUtils.object2String(update));

        BizNews getOne = bizNewsDomainService.getById(save.getId());
        log.info("查询的数据 getOne ：\n{}", JacksonUtils.object2String(getOne));

        bizNewsDomainService.deleteById(save.getId());
        log.info("查询的数据 deleteById ：\n{}", JacksonUtils.object2String(save));

//        BizNews getByName = jpaDemoService.getByName("123");
//        log.info("查询的数据 getByName ：\n{}", JacksonUtils.object2String(getByName));

        saveAll.forEach(item -> item.setTitle("saveAll-test-update"));
        List<BizNews> updateList = bizNewsDomainService.update(saveAll, Sets.newHashSet("name"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));

        List<BizNews> findAll = bizNewsDomainService.list();
        log.info("查询的数据 findAll ：\n{}", JacksonUtils.object2String(findAll));

        Map<String, Object> filterParams = Maps.newHashMap();
        filterParams.put("name", "saveAll-test-update");
        List<BizNews> findByFilter = bizNewsDomainService.list(filterParams);
        log.info("查询的数据 findByFilter: {}", JacksonUtils.object2String(filterParams));
        log.info("查询的数据 findByFilter: {}", JacksonUtils.object2String(findByFilter));


        bizNewsDomainService.deleteByIds(findAll.stream().map(BizNews::getId).collect(Collectors.toList()));
        log.info("查询的数据 deleteById ：\n{}", JacksonUtils.object2String(save));

    }


    @Test
    public void deleteByParam() {
        List<BizNews> saveAll = bizNewsDomainService.save(Lists.newArrayList(
                new BizNews("delete-param-test", new Date()),
                new BizNews("delete-param-test", new Date())
        ));

        Map<String, Object> map = Maps.newHashMap();
        map.put("name", "delete-param-test");

        log.info("删除前查询到数据为 {}", JacksonUtils.object2String(bizNewsDomainService.list(map)));
        bizNewsDomainService.delete(map);
        log.info("删除后查询到数据为 {}", JacksonUtils.object2String(bizNewsDomainService.list(map)));

    }

}
