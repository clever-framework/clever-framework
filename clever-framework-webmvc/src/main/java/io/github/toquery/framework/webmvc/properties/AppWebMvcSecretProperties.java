package io.github.toquery.framework.webmvc.properties;

import io.github.toquery.framework.webmvc.secret.constants.AppSecretConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 配置请求、响应加密。包含6种情况
 * 1. 全局加密，请求不加密，响应加密
 * 3. 全局加密，请求不加密，响应不加密
 * 2. 全局加密，请求加密，响应不加密
 * 4. 全局不加密，请求加密，响应加密
 * 5. 全局不加密，请求加密，响应不加密
 * 6. 全局不加密，请求不加密，响应加密
 *
 * @author toquery
 */
@Setter
@Getter
@Slf4j
public class AppWebMvcSecretProperties {

    private AppWebMvcSecretItem request = new AppWebMvcSecretItem(false, AppSecretConstants.REQ_AES_KEY, AppSecretConstants.REQ_AES_IV);

    private AppWebMvcSecretItem response = new AppWebMvcSecretItem(false, AppSecretConstants.RES_AES_KEY, AppSecretConstants.RES_AES_IV);

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AppWebMvcSecretItem {

        /**
         * 加密
         */
        private boolean enabled = false;

        /**
         * 密钥长度不得小于24
         */
        private String key;

        /**
         * IV向量必须为8位
         */
        private String iv;
    }
}
