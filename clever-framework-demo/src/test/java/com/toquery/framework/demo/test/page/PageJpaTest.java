package com.toquery.framework.demo.test.page;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.toquery.framework.demo.entity.TbJpaDemo;
import com.toquery.framework.demo.service.IJpaDemoService;
import com.toquery.framework.demo.test.BaseSpringTest;
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
    private IJpaDemoService jpaDemoService;

    @Test
    public void insertData() {
        int size = 12;

        List<TbJpaDemo> saveAll = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            TbJpaDemo tbJpaDemo = new TbJpaDemo("test-page-" + (i + 1),size - i - 1, new Date());
            saveAll.add(tbJpaDemo);
        }
        saveAll = jpaDemoService.save(saveAll);
        log.info("插入的数据 saveBatch ：\n{}", JSON.toJSONString(saveAll));
    }
}
