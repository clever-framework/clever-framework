package io.toquery.framework.example.test.java.threadlock;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 *
 */
public class SemaphoreTest {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("韦小宝");
        list.add("阿珂");
        list.add("双儿");
        list.add("曾柔");
        list.add("建宁公主");
        list.add("沐建屏");
        list.add("方怡");
        list.add("苏荃");

        Semaphore semaphore = new Semaphore(4, true);
        for (int i = 0; i < 8; i++) {
            new Thread(() -> {
                try {
                    semaphore.acquire();
                    String user = list.remove(new Random().nextInt(list.size()));
                    System.out.println("韦小宝带着七个老婆，过着桃园生活。每天打打麻将、练练武术。麻将四人桌：" + user);
                    Thread.sleep(3000L);
                } catch (InterruptedException ignore) {
                } finally {
                    semaphore.release();
                }
            }).start();
        }

    }
}
