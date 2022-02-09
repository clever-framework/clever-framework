package io.toquery.framework.example.test.framework.datasource;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.toquery.framework.example.bff.admin.author.info.service.BizAuthorService;
import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import com.toquery.framework.example.modules.author.info.service.BizAuthorDomainService;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.toquery.framework.example.test.BaseSpringTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
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
public class BizAuthorServiceCurdTest extends BaseSpringTest {

    @Resource
    private BizAuthorService bizAuthorService;


    @Resource
    private BizAuthorDomainService bizAuthorDomainService;

    @Test
    public void updatePart() {
        BizAuthor bizAuthor = new BizAuthor("update-part-test");
        bizAuthorDomainService.save(bizAuthor);

        BizAuthor updatePart = new BizAuthor();
        updatePart.setId(bizAuthor.getId());
        BizAuthor newUpdate = bizAuthorDomainService.update(updatePart, Sets.newHashSet("authorName"));

        log.info("查询的数据 newUpdate ：\n{}", JacksonUtils.object2String(bizAuthorDomainService.getById(newUpdate.getId())));

    }

    @Test
    public void updatePartByJPA() {
        BizAuthor BizAuthor = new BizAuthor("update-part-test");
        bizAuthorDomainService.save(BizAuthor);


        log.info("查询的数据 BizAuthor ：\n{}", JacksonUtils.object2String(bizAuthorDomainService.getById(BizAuthor.getId())));

        BizAuthor updatePart = new BizAuthor();
        updatePart.setId(BizAuthor.getId());

        BizAuthor newUpdate = bizAuthorService.updateJpa(updatePart);

        log.info("查询的数据 newUpdate ：\n{}", JacksonUtils.object2String(bizAuthorDomainService.getById(newUpdate.getId())));
    }

    @Test
    public void curd() {
        BizAuthor save = bizAuthorDomainService.save(new BizAuthor("save-test"));
        log.info("插入的数据 save ：\n{}", JacksonUtils.object2String(save));

        List<BizAuthor> saveAll = bizAuthorDomainService.save(Lists.newArrayList(
                new BizAuthor("saveAll-test"),
                new BizAuthor("saveAll-test")
        ));
        log.info("插入的数据 saveBatch ：\n{}", JacksonUtils.object2String(saveAll));

        BizAuthor updateEntity = bizAuthorService.update(save.getId(), "update-entity");
        log.info("插入的数据 updateEntity ：\n{}", JacksonUtils.object2String(updateEntity));

        save.setAuthorName("save-test-update");
        BizAuthor update = bizAuthorDomainService.update(save, Sets.newHashSet("authorName"));
        log.info("修改的数据 update ：\n{}", JacksonUtils.object2String(update));

        BizAuthor getOne = bizAuthorDomainService.getById(save.getId());
        log.info("查询的数据 getOne ：\n{}", JacksonUtils.object2String(getOne));

        bizAuthorDomainService.deleteById(save.getId());
        log.info("查询的数据 deleteById ：\n{}", JacksonUtils.object2String(save));

//        BizAuthor getByName = jpaDemoService.getByName("123");
//        log.info("查询的数据 getByName ：\n{}", JacksonUtils.object2String(getByName));

        saveAll.forEach(item -> item.setAuthorName("saveAll-test-update"));
        List<BizAuthor> updateList = bizAuthorDomainService.update(saveAll, Sets.newHashSet("authorName"));
        log.info("修改的数据 updateList ：\n{}", JacksonUtils.object2String(updateList));

        List<BizAuthor> findAll = bizAuthorDomainService.list();
        log.info("查询的数据 findAll ：\n{}", JacksonUtils.object2String(findAll));

        Map<String, Object> filterParams = Maps.newHashMap();
        filterParams.put("name", "saveAll-test-update");
        List<BizAuthor> findByFilter = bizAuthorDomainService.list(filterParams);
        log.info("查询的数据 findByFilter: {}", JacksonUtils.object2String(filterParams));
        log.info("查询的数据 findByFilter: {}", JacksonUtils.object2String(findByFilter));


        bizAuthorDomainService.deleteByIds(findAll.stream().map(BizAuthor::getId).collect(Collectors.toList()));
        log.info("查询的数据 deleteById ：\n{}", JacksonUtils.object2String(save));

    }


    @Test
    public void deleteByParam() {
        List<BizAuthor> saveAll = bizAuthorDomainService.save(Lists.newArrayList(
                new BizAuthor("delete-param-test"),
                new BizAuthor("delete-param-test")
        ));

        Map<String, Object> map = Maps.newHashMap();
        map.put("authorName", "delete-param-test");

        log.info("删除前查询到数据为 {}", JacksonUtils.object2String(bizAuthorDomainService.list(map)));
        bizAuthorDomainService.delete(map);
        log.info("删除后查询到数据为 {}", JacksonUtils.object2String(bizAuthorDomainService.list(map)));

    }

}
