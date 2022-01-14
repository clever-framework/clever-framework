package io.github.toquery.framework.webmvc.secret.constants;

/**
 * 加密常量
 */
public interface AppSecretConstants {
    /**
     * 请求密钥
     */
    String REQ_AES_KEY = "fb21f92598c745d192ef3dd7dad59cae";
    /**
     * 请求偏移量
     */
    String REQ_AES_IV = "8f42f9081cb02d0e";


    /**
     * 响应密钥
     */
    String RES_AES_KEY = "062a6cfae5ae45f286b45440d5c25fcb";
    /**
     * 响应偏移量
     */
    String RES_AES_IV = "f15b68acf3e3aa02";
}
