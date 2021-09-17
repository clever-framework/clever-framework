package io.github.toquery.framework.webmvc.service;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.LineCaptcha;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.google.common.base.Strings;
import io.github.toquery.framework.core.captcha.AbstractCaptchaService;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.webmvc.domain.CaptchaResponse;
import io.github.toquery.framework.webmvc.properties.AppWebMvcProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

/**
 * token 格式 时间戳:code:salt
 * 例如：1631875996078:ADCD:xxxxxx
 */
@Slf4j
public class CaptchaService extends AbstractCaptchaService {

    private static final String SEPARATION = ":";

    private static final String EXPIRED_SEPARATION = "_";

    @Autowired
    protected AppWebMvcProperties appWebMvcProperties;

    /**
     * 验证图形验证码是否有效
     *
     * @param captchaValue 验证码
     * @param tokenStr     token
     * @return true or false
     */
    public boolean valid(String captchaValue, String tokenStr) {
        if (Strings.isNullOrEmpty(captchaValue)) {
            throw new AppException("验证码不能为空");
        }
        if (Strings.isNullOrEmpty(tokenStr)) {
            throw new AppException("验证码token不能为空");
        }
        if (!tokenStr.contains(EXPIRED_SEPARATION)) {
            throw new AppException("验证码token格式错误");
        }

        String[] tokens = tokenStr.split(EXPIRED_SEPARATION);
        String token = tokens[0];
        String tokenTime = tokens[1];

        // 获取token的过期时间
        LocalDateTime tokenLocalDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(tokenTime)), ZoneId.systemDefault()).plusMinutes(appWebMvcProperties.getCaptcha().getExpired());

        if (tokenLocalDateTime.isBefore(LocalDateTime.now())){
            throw new AppException("验证码已过期");
        }

        String tokenCheck = this.createToken(captchaValue.toUpperCase(), tokenTime);

        return token.equalsIgnoreCase(tokenCheck);
    }

    /**
     * 生成图形验证码
     *
     * @return 图形验证码bean
     */
    public CaptchaResponse generateCaptcha() {
        AppWebMvcProperties.AppWebMvcCaptchaProperties appWebMvcCaptchaProperties = appWebMvcProperties.getCaptcha();
        //定义图形验证码的长和宽
        LineCaptcha lineCaptcha = CaptchaUtil.createLineCaptcha(appWebMvcCaptchaProperties.getWidth(), appWebMvcCaptchaProperties.getHeight());
        lineCaptcha.setGenerator(new RandomGenerator(appWebMvcCaptchaProperties.getRandomCode(), appWebMvcCaptchaProperties.getLength()));
        String createTime = String.valueOf(System.currentTimeMillis());
        String captchaBase64 = imgToBase64(lineCaptcha.getImage());
        String token = this.createToken(lineCaptcha.getCode().toUpperCase(), createTime);
        return new CaptchaResponse(token + EXPIRED_SEPARATION + createTime, "data:image/png;base64," + captchaBase64);
    }


    protected String createToken(String captchaCode, String createTime) {
        String tokenPrefix = createTime + SEPARATION + captchaCode + appWebMvcProperties.getCaptcha().getSalt();
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String token = md5.digestHex(tokenPrefix + EXPIRED_SEPARATION + createTime);
        log.debug("生成验证码 code {} token {}", captchaCode, token);
        return token;
    }

    /**
     * 图片转base64
     *
     * @param img 图片
     * @return base64
     */
    protected String imgToBase64(RenderedImage img) {
        String imageBase64 = null;
        try (
                ByteArrayOutputStream os = new ByteArrayOutputStream()
        ) {
            ImageIO.write(img, "png", os);
            imageBase64 = Base64.getEncoder().encodeToString(os.toByteArray());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return imageBase64;
    }
}
