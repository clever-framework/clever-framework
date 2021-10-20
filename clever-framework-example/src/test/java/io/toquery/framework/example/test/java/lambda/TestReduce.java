package io.toquery.framework.example.test.java.lambda;

import com.google.common.collect.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public class TestReduce {

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
        List<Integer> ints = Lists.newArrayList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        System.out.println("ints sum is:" + ints.stream().reduce(0, (a, b) -> {
            System.out.println(a + "+" + b + "=" + (a + b));
            return a + b;
        }));
    }
}
