package com.toquery.framework.demo.test.thread;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class TestThreadBug {


    private static boolean fuck = true;

    @Test
    public void test() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (TestThreadBug.fuck) {
                    i++;
                    log.info("i am inner run {}", i);
                    System.out.println("i am inner run" + i);
                }
            }
        }).start();
        Thread.sleep(3000);
        TestThreadBug.fuck = false;
        System.out.println("over");
        log.info("i am over");
    }
}
