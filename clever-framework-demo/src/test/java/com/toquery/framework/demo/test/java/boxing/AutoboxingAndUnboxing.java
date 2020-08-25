package com.toquery.framework.demo.test.java.boxing;

import org.junit.Test;

/**
 * @author toquery
 * @version 1
 */
public class AutoboxingAndUnboxing {

    /**
     * http://blog.oneapm.com/apm-tech/635.html
     *
     * total:2305843005992468481
     * processing time: 9056 ms
     * total:2305843005992468481
     * processing time: 688 ms
     */
    @Test
    public void test() {
        long t = System.currentTimeMillis();
        Long sum = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum += i;
        }
        System.out.println("total:" + sum);
        System.out.println("processing time: " + (System.currentTimeMillis() - t) + " ms");


        long t2 = System.currentTimeMillis();//Long sum = 0L;
        long sum2 = 0L;
        for (long i = 0; i < Integer.MAX_VALUE; i++) {
            sum2 += i;
        }
        System.out.println("total:" + sum2);
        System.out.println("processing time: " + (System.currentTimeMillis() - t2) + " ms");
    }
}
