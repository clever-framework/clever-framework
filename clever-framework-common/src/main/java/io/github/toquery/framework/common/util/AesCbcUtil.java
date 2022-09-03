package io.github.toquery.framework.common.util;

import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;

/**
 * AES加密解密
 */
@Slf4j
public class AesCbcUtil {

    /**
     * @param plainText 普通文本
     * @param secretKey 密钥
     * @param iv        向量
     * @return 加密后的文本，失败返回null
     */
    public static String encode(String plainText, String secretKey, String iv) {
        String result = null;
        try {
            AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKey.getBytes(), iv.getBytes());
            result = aes.encryptBase64(plainText);
        } catch (Exception e) {
            log.error("AesCbcUtil encode error", e);
        }
        return result;
    }

    /**
     * AES解密
     *
     * @param encryptText 加密文本
     * @param secretKey   密钥
     * @param iv          向量
     * @return 解密后明文，失败返回null
     */
    public static String decode(String encryptText, String secretKey, String iv) {
        String result = null;
        try {
            AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, secretKey.getBytes(), iv.getBytes());
            result = aes.decryptStr(encryptText);
        } catch (Exception e) {
            log.error("AesCbcUtil decode error", e);
        }
        return result;
    }

}
