package io.toquery.framework.example.test.java.threadlock;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 对两种资源使用同一个锁，
 * 但不允许写线程和读线程、写线程和写线程同时访问。
 *
 * 写入锁、写入数据、写入解锁
 * 读取锁、读取数据、读取解锁
 *
 */
public class ReentrantReadWriteLockTest {
    private static final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static final Lock readLock = readWriteLock.readLock();
    private static final Lock writeLock = readWriteLock.writeLock();

    private static Deque<String> deque = new ArrayDeque<>();

    public static String get() {
        readLock.lock();
        try {
            return deque.poll();
        } finally {
            readLock.unlock();
        }
    }

    public static void put(String value) {
        writeLock.lock();
        try {
            deque.add(value);
        } finally {
            writeLock.unlock();
        }
    }

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                put("小擒拿");
                put("抓乃龙抓手");
                put("下脚绊");
                put("大别子");
                put("锁喉");
                put("扣眼珠子");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ignore) {
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                System.out.println("韦小宝与皇上比武出招：" + get());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ignore) {
                }
            }
        }).start();
    }
}
