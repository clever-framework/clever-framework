package io.toquery.framework.common;

import io.github.toquery.framework.common.util.AesCbcUtil;
import org.junit.Test;

/**
 *
 */
public class TestAesCbcUtil {

    public static final String key = "062a6cfae5ae45f286b45440d5c25fcb";
    public static final String iv = "f15b68acf3e3aa02";

    @Test
    public void testEncryptAndDecrypt() {
        String plainText = "123";
        String cipherText = AesCbcUtil.encode(plainText, key, iv);
        System.out.println("cipherText: " + cipherText);
        String plainText2 = AesCbcUtil.decode(cipherText, key, iv);
        System.out.println("plainText2: " + plainText2);
    }

    @Test
    public void encode() {
        String plainText = "123";
        String cipherText = AesCbcUtil.encode(plainText, key, iv);
        System.out.println("cipherText: " + cipherText);
    }

    @Test
    public void decode() {
        String cipherText = "uMBM33RIXQGq1XESJql4/IUoGLa1/MblIfvkzZFK1DU=";
        String plainText = AesCbcUtil.decode(cipherText, key, iv);
        System.out.println("plainText2: " + plainText);
    }
}
