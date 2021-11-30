package io.toquery.framework.example.test.framework.page;

import com.google.common.collect.Lists;
import com.toquery.framework.example.modules.news.entity.BizNews;
import com.toquery.framework.example.modules.news.service.BizNewsDomainService;
import io.toquery.framework.example.test.BaseSpringTest;
import io.github.toquery.framework.common.util.JacksonUtils;
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
public class PageJpaTest extends BaseSpringTest {


    @Resource
    private BizNewsDomainService jpaDemoService;

    @Test
    public void insertData() {
        int size = 12;

        List<BizNews> saveAll = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            BizNews tbJpaDemo = new BizNews("test-page-" + (i + 1), new Date());
            saveAll.add(tbJpaDemo);
        }
        saveAll = jpaDemoService.save(saveAll);
        log.info("插入的数据 saveBatch ：\n{}", JacksonUtils.object2String(saveAll));
    }
}
