package com.toquery.framework.example.test.java.thread;

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


    //卖票程序的同步代码块实现示例
    class Ticket implements Runnable {
        // 定义100张票
        private int tickets = 10;

        public void run() {
            while (true) {
                sellTicket();
            }
        }

        private synchronized void sellTicket() {
            if (tickets > 0) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "正在出售第" + (tickets--) + "张票 ");
            }
        }
    }

    @Test
    public void testTestThreadSynchronized() {
        // 通过Thread类创建线程对象，并将Runnable接口的子类对象作为Thread类的构造函数的参数进行传递。
        Ticket t = new Ticket();
        Thread t1 = new Thread(t);
        Thread t2 = new Thread(t);
        Thread t3 = new Thread(t);
        Thread t4 = new Thread(t);
        // 调用线程对象的start方法开启线程。
        t1.start();
        t2.start();
        t3.start();
        t4.start();
    }
}
