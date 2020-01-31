package com.toquery.framework.demo.test.thread;

import org.junit.Test;

public class TestThread {


    private static boolean fuck = true;

    @Test
    public void test() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (TestThread.fuck) {
                    i++;
                }
                System.out.println("i am inner run" + i);
            }
        }).start();
        Thread.sleep(3000);
        TestThread.fuck = false;
        System.out.println("over");
    }
}
