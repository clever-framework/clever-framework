package io.github.toquery.framework.common.util;

import cn.hutool.core.lang.id.NanoId;

import java.security.SecureRandom;

/**
 *
 */
public final class NanoIdUtils {
    public static final SecureRandom DEFAULT_NUMBER_GENERATOR = new SecureRandom();
    // 随机生成的字符内容
    public static final char[] DEFAULT_ALPHABET = "23456789abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ".toCharArray();

    // 随机生成 ID 的位数
    public static final int DEFAULT_SIZE = 16;

    private NanoIdUtils() {
    }

    public static String randomNanoId() {
        return NanoId.randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, DEFAULT_SIZE);
    }

    public static String randomNanoId(int size) {
        return NanoId.randomNanoId(DEFAULT_NUMBER_GENERATOR, DEFAULT_ALPHABET, size);
    }

}
