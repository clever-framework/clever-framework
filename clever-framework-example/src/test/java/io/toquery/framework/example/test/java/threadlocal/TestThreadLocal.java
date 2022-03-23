package io.toquery.framework.example.test.java.threadlocal;


import org.junit.Test;

/**
 *
 */
public class TestThreadLocal {

    private static final int HASH_INCREMENT = 0x61c88647;

    @Test
    public void test_idx() {
        int hashCode = 0;
        for (int i = 0; i < 16; i++) {
            hashCode = i * HASH_INCREMENT + HASH_INCREMENT;
            int idx = hashCode & 15;
            System.out.println("斐波那契散列：" + idx + " 普通散列：" + (String.valueOf(i).hashCode() & 15));
        }
    }

}
