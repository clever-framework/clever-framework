package com.toquery.framework.demo.test.collection;

import com.alibaba.fastjson.JSON;
import com.google.api.client.util.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;

@Slf4j
public class TestLink {


    /**
     * 15:28:59.028 [main] INFO com.toquery.framework.demo.test.collection.TestLink -  item为 id 1 , name null, 内存地址为: com.toquery.framework.demo.test.collection.TestLink$JavaLink@737996a0
     * 15:28:59.083 [main] INFO com.toquery.framework.demo.test.collection.TestLink -  item为 id 3 , name null, 内存地址为: com.toquery.framework.demo.test.collection.TestLink$JavaLink@737996a0
     * 15:28:59.083 [main] INFO com.toquery.framework.demo.test.collection.TestLink -  bigClass = item 为 true , bigClass2 = item 为 true
     * 15:28:59.083 [main] INFO com.toquery.framework.demo.test.collection.TestLink -  item为 id 3 , name null, 内存地址为: com.toquery.framework.demo.test.collection.TestLink$JavaLink@737996a0
     * 15:28:59.083 [main] INFO com.toquery.framework.demo.test.collection.TestLink -  bigClass = item 为 true , bigClass2 = item 为 true
     * 15:28:59.083 [main] INFO com.toquery.framework.demo.test.collection.TestLink -  item为 id 3 , name null, 内存地址为: com.toquery.framework.demo.test.collection.TestLink$JavaLink@737996a0
     * 15:28:59.083 [main] INFO com.toquery.framework.demo.test.collection.TestLink -  bigClass = item 为 true , bigClass2 = item 为 true
     * [{"id":"3"},{"$ref":"$[0]"},{"$ref":"$[0]"}]
     */
    @Test
    public void test1() {
        JavaLink bigClass = new JavaLink();
        List<JavaLink> list = Lists.newArrayList();
        list.add(bigClass);
        // 引用对象
        JavaLink bigClass2 = bigClass;

        // 实际为同一个
        bigClass.setId("3");
        bigClass2.setId("1");

        log.info(" item为 id {} , name {}, 内存地址为: {}", bigClass2.getId(), bigClass2.getName(), bigClass2.toString());
        list.add(bigClass2);

        list.forEach(item -> {
            log.info(" item为 id {} , name {}, 内存地址为: {}", item.getId(), item.getName(), item.toString());
            log.info(" bigClass = item 为 {} , bigClass2 = item 为 {} ", bigClass == item, bigClass2 == item);
        });

        System.out.println(JSON.toJSONString(list));
    }

    @Getter
    @Setter
    //@ToString
    //@EqualsAndHashCode
    class JavaLink {

        private String id;
        private String name;

    }
}
