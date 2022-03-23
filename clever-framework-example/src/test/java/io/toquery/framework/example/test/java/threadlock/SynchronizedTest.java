package io.toquery.framework.example.test.java.threadlock;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class SynchronizedTest {
    private static ExecutorService LCY = Executors.newFixedThreadPool(10);

    private static volatile boolean LB = false;

    public static class KG implements Runnable {

        private String name;

        public KG(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                balabala(name);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    public static synchronized void balabala(String name) throws InterruptedException {
        while (true){
            System.out.println("韦春花与" + name + "喝茶、吟诗、做对、聊风月！");
            if (LB){
                System.out.println("老鸨敲门：时间到啦！\r\n");
                LB = false;
                break;
            }
            Thread.sleep(1000);
        }

    }

    private static List<String> list = Arrays.asList("鳌大人", "陈近南", "海大富");

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            LCY.execute(new KG(list.get(i)));
            Thread.sleep(3000);
            LB = true;
        }
    }
}
