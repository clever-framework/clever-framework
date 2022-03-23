package io.toquery.framework.example.test.java.threadlock;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class ReentrantLockTest {

    private static ReentrantLock lock = new ReentrantLock(true);

    private static List<String> list = Arrays.asList("路人甲", "路人乙", "路人丙", "路人丁", "路人戊", "路人己", "路人庚", "路人壬", "路人癸", "韦小宝");

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            int idx = i;

            new Thread(() -> {
                try {
                    招收杂役(list.get(idx));
                } catch (InterruptedException ignore) {
                }
            }).start();

            if (idx == 9) {
                new Thread(() -> {
                    招收太监(list.get(idx));
                }).start();
            }
        }
    }

    public static void 招收杂役(String name) throws InterruptedException {
        lock.lock();
        try {
            while (true) {
                System.out.println(name + "，排队等待进宫当杂役...");
                Thread.sleep(1000);
            }
        } finally {
            lock.unlock();
        }
    }

    public static void 招收太监(String name) {
        System.out.println(name + "，进宫当太监，不用排队！");
    }
}
