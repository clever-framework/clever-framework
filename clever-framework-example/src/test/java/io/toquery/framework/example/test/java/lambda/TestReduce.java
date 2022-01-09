package io.toquery.framework.example.test.java.lambda;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class TestReduce {

    private List<Integer> ints = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    /**
     * 0+1=1
     * 1+2=3
     * 3+3=6
     * 6+4=10
     * 10+5=15
     * 15+6=21
     * 21+7=28
     * 28+8=36
     * 36+9=45
     * 45+10=55
     * ints sum is:55
     */
    @Test
    public void testReduce01() {
        Integer sum = ints.stream().reduce(0, (a, b) -> {
            log.info(a + "+" + b + "=" + (a + b));
            return a + b;
        });
        System.out.println("ints sum is:" + sum);
    }

    /**
     * 1+5=6
     * 6+6=12
     * 12+7=19
     * 19+8=27
     * 27+9=36
     * 36+10=46
     */
    @Test
    public void testReduce02() {
        // 相当于foreach遍历操作结果值
        Integer out = ints.stream().reduce((result, item) -> {
            // log.info("result {} item {} ", result, item);
            if (item >= 5) {
                System.out.println(result + "+" + item + "=" + (result + item));
                result = result + item;
            }
            return result;
        }).get();
        System.out.println(out);
    }

    /**
     * 并行计算
     * accumulator: 1+2=3
     * accumulator: 1+3=4
     * combiner: 3+4=7
     * accumulator: 1+1=2
     * combiner: 2+7=9
     * 9
     */
    @Test
    public void testReduce03() {
        //相当于给定初始结果值，两个foreach遍历操作结果值
        int str = Stream.of(1,2,3).parallel().reduce(1, (result, item) -> {
            System.out.println("accumulator: " + result + "+" + item + "=" + (result + item));
            return result + item;
        } , (result, item) -> {
            //注：只有并行parallel下才会进入此方法
            System.out.println("combiner: " + result + "+" + item + "=" + (result + item));
            return result + item ;
        });
        System.out.println(str);
    }
}
