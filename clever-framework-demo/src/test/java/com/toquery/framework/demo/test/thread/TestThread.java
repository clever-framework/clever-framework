package com.toquery.framework.demo.test.thread;

import org.junit.Test;

public class TestThread {

    @Test
    public void testRunnableImpl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("i am run in the thead with runnable !");
            }
        }).start();
        System.out.println("i am run over");
    }

    static class TestThreadImpl extends Thread {
        @Override
        public void run() {
            System.out.println("i am run in the thead with thread !");
        }
    }

    @Test
    public void testTestThreadImpl() {
        new TestThreadImpl().start();
        System.out.println("i am run over");
    }
}
