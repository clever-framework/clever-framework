package io.github.toquery.framework.dao.util;

import com.google.common.base.Strings;
import com.google.common.primitives.Longs;

public class UtilNumber {//extends NumberUtils {

    /**
     * 获取二进制长度标示的最大十进制数
     *
     * @param binaryLength 二进制长度
     * @return 最大十进制数
     */
    public static int getMaxNum(int binaryLength) {
        return (1 << binaryLength) - 1;
    }

    /**
     * long转换为2进制
     *
     * @param num long
     * @return 2进制
     */
    public static String toBinary(long num) {
        return Long.toString(num, 2);
    }

    /**
     * int转2进制
     *
     * @param num int
     * @return 2进制
     */
    public static String toBinary(int num) {
        return Integer.toString(num, 2);
    }

    /**
     * string转换为字符串
     *
     * @param str 字符串
     * @return 二进制字符串
     */
    public static String toBinary(String str) {
        if (Strings.isNullOrEmpty(str)) {
            return null;
        }
        str = str.trim();
        Long num = Longs.tryParse(str);
        if (num != null) {
            return toBinary(num);
        } else {
            //将字符串转换为字符数组
            char[] chars = str.toCharArray();
            StringBuffer binaryStr = new StringBuffer();
            for (char item : chars) {
                binaryStr.append(Integer.toBinaryString(item));
            }
            return binaryStr.toString();
        }
    }

}
