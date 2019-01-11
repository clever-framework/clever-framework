package com.toquery.framework.demo.test.lambda;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class TestIntellJIDEAStreamDebugger {

    @Test
    public void test1() {
        Stream<String> s0 = Stream.of("C", "B", "A"); // "C", "A", "B"
        Stream<String> s1 = s0.sorted(); // "A", "B", "C"
        List<String> strings = s1.collect(toList()); // [“A”, “B”, “C”]
    }

    @Test
    public void test2() {
        List<String> strings = Stream.of("C", "B", "A").sorted().collect(toList()); // The final result is saved to step 2
    }

    @Test
    public void test3() {
        List<String> list = Lists.newArrayList("B", "D", "C", "A");
        List<String> strings = list.parallelStream().sorted().collect(toList());
        List<String> string2 = list.stream().sorted().collect(toList());
    }


    @Test
    public void test4() {
        List<String> list1 = Lists.newArrayList("B", "D", "C", "A");
        Map<String,List<String>> map = Maps.newHashMap();
        map.put("A",Lists.newArrayList("A1", "A2", "A3", "A4"));
        map.put("B",Lists.newArrayList("B1", "B2", "B3", "B4"));
        map.put("C",Lists.newArrayList("C1", "C2", "C3", "C4"));
        map.put("D",Lists.newArrayList("D1", "D2", "D3", "D4"));
        List<String> strings = list1.stream().flatMap(item -> map.get(item).stream()).sorted().collect(toList());
        log.info(JSON.toJSONString(strings));
    }


}
