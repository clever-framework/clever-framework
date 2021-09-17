package io.github.toquery.framework.core.captcha;


/**
 *
 */
public abstract class AbstractCaptchaService {

    public static final String CAPTCHA_CODES = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    /**
     * 验证图形验证码是否有效
     *
     * @param captchaValue 验证码
     * @param token        token
     * @return true or false
     */
    public abstract boolean valid(String captchaValue, String token);

}
