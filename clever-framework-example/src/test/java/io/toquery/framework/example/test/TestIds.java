package io.toquery.framework.example.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
@Slf4j
public class TestIds {

    @Test
    public void test() throws InterruptedException {
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);

        Set<String> sets = new HashSet<>();

        for (int i = 0; i < 10; i++) {
            fixedThreadPool.execute(() -> {
                String key = test(UUID.randomUUID().toString(), 11);
                sets.add(key);
                System.out.println(key);
                log.info("{}", key);
            });
        }
        Thread.sleep(1000 * 30);
        fixedThreadPool.shutdown();
    }

    public static String test(String target, int length) {
        target = target.replaceAll("-", "");
        if (target.length() >= length) {
            return target.substring(0, length);
        }
        StringBuffer sb = new StringBuffer("xxxx");
        for (int i = 0; i < length - (1 + target.length()); i++) {
            sb.append(get());
        }
        return sb.append(target).toString();
    }

    public static int get() {
        return new Random(1).nextInt();
    }
}
