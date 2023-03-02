package io.github.toquery.framework.security.properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.security.constant.TokenTypeConstant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Data
@Component
@ConfigurationProperties(prefix = AppSecurityProperties.PREFIX)
public class AppSecurityProperties {

    public static final String PREFIX = "app.security";

    private boolean enabled = true;

    /**
     * 是否启用 defaultAuthenticationManager
     */
    private boolean enabledDAF = true;

    /**
     * 是否启用 jwtEncoder
     */
    private boolean enabledDJE = true;

    /**
     * 是否启用 jwtDecoder
     */
    private boolean enabledDJD = true;

    /**
     * 是否启用 jwtTokenProvider
     */
    private boolean enabledDJP = true;


    /**
     * 是否启用 defaultSecurityFilterChain
     */
    private boolean enabledDSFC = true;

    /**
     * 是否开启用户注册，默认不开启
     */
    private boolean register = false;

    /**
     *
     */
    private TokenTypeConstant tokenType = TokenTypeConstant.JWT;
    /**
     * 配置白名单
     */
    private Set<String> ignoring = Sets.newHashSet();

    //url权限配置
    private List<UrlAuthConfig> urlAuths = Lists.newArrayList();

    private AppSecurityJwtKey key = new AppSecurityJwtKey();

    @Getter
    @Setter
    public static class AppSecurityJwtKey {

        private String keyId = "123";

        private String issuer = "clever-framework";
        private String publicKey = """
                -----BEGIN PUBLIC KEY-----
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtsalNOFf2ot/gChhNFNx
                Frp2u32d91YhmkZ987zOKYQOlS1OfjHTYu9FwCAwOLhUpIlESXMRM6wPCe/5qv+B
                RVuA0voWrR+ZydvYk0lfrrGv4YGY7gedqiNv5U9W0cZf+D0S5vcrDJtXSYsM5fzK
                rMqVX+FbEP7g91QmitwrOqCj5x/m8KqOhLEKXCSxLJunJH1Jzf8hfsX8fwradDc8
                19JwRzj8GsojmVu5XZ8ji3uwFcSwRS0xOaOrOMwiSi47+ABm01wn4nPYuOvYmg+k
                9Dois8El5Dm8opyduEyMYJ6eqLtMk/PW34fsWWio9mDFNLqt9XBg+Cc9JJYlPI+e
                ZwIDAQAB
                -----END PUBLIC KEY-----
                """;

        private String privateKey = """
                -----BEGIN PRIVATE KEY-----
                MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC2xqU04V/ai3+A
                KGE0U3EWuna7fZ33ViGaRn3zvM4phA6VLU5+MdNi70XAIDA4uFSkiURJcxEzrA8J
                7/mq/4FFW4DS+hatH5nJ29iTSV+usa/hgZjuB52qI2/lT1bRxl/4PRLm9ysMm1dJ
                iwzl/MqsypVf4VsQ/uD3VCaK3Cs6oKPnH+bwqo6EsQpcJLEsm6ckfUnN/yF+xfx/
                Ctp0NzzX0nBHOPwayiOZW7ldnyOLe7AVxLBFLTE5o6s4zCJKLjv4AGbTXCfic9i4
                69iaD6T0OiKzwSXkObyinJ24TIxgnp6ou0yT89bfh+xZaKj2YMU0uq31cGD4Jz0k
                liU8j55nAgMBAAECggEAN23ovi+3JQz3HsOgLwE7I5xq05NV66N4T1QfcGKK1zZQ
                QddanZS4uaE8svZQCXOFX9ytE+VMizIb4XSgb8ZKyuDkIZdsdxZFSl1cwdYXqWAD
                qczmtxEiNG3Iym8UMkwJ4LuM1vSQXqNhhlEgGUNfp6VMRaaPOolBamBfy+7XQVDg
                goTr8NLLA6rCCXzJ7uFHe2to7nARAw4Tm2nnttEDn4KJpfLYn3b7yilwciqsZ3yp
                uMm5C+RRd9cHsMelSaVGKnARsf4MYpvrbV7nZuPczdcCVtNoH68OIIfB960KMjyT
                C2+wXXGWahRh1o3ZFVbyE3kVdKaunIiAP6Ex1dG6KQKBgQDtJGavxjsYzlRkBRhK
                TZmynLvDRPChJ4XmgFn9Me+s+ctROj+Rjg/Enw41N7PmGeBfqr2C4mzfzrPt1BVm
                2dqe5GR9lI/F1AZnJEf6hqOJVoAFDDBJcxcIAMX/s3DKULln98wGd04Da8ERMo5o
                WEksbQNnaxBc0CilZWd+AD6y8wKBgQDFT30sbTyzKMhIdrlN5myP/S0PcbeaiceH
                XN3UF3FGuZzRA6sOhwOegKS8Mj53arb6kE+1pAMV/3fQxKS5cESSUzkJ5YbjSnWp
                NTD58GWU1p9i8N9Ao2FOqZ6h1sJOzcUSuoi4zFajc25/Mtx496MWWMYqlJMcHHcK
                iUI9D7q7vQKBgF3lJNeCONIImDeGIOkCstGa52lYuaJFgOoUdg0foZOu0EcYNiVO
                x4WFqN1/cbaxFjsq4qCZQJyZPnrzl4nCxqJVMkja8tZUrjhWkD1s5AG0Azp8af4b
                GMpta/hjt0wUDGVTIzCIOpZ2KtrBVsuP8WEqxMdFCBSHwyBB92lXdXvrAoGAZOJ4
                yQ9tetl+VnbF1ovbtF+p+3yUeSK7b33POlNmKtha0xACFWJ5OkDiegtinyJkPoct
                Cl266jN+KrNw2hQKK5r7zhCawHpg4Mx+pz0AY3K9ehRYwi8HYPYfUvaA0VQq0KGp
                qHtqTRaHR+hUjFFqBNINONZQxY3UXAHEaX4yZy0CgYEAzgqwyfsfWTGJss+79DIJ
                o7k+CGgvIGxPGKSBvNC/hgFNj4PpALEY3B+KASC09sE94AZDRRe2ZN2h0qFiKyAI
                m0KguopMFLdQi/G4x7vZR4g8zuaw3OsGhm1lo7X5HiHvF40j1dgVSpikxNR6DyTa
                dLQxoJtwnhTYI2zjEjvcpz4=
                -----END PRIVATE KEY-----
                """;

    }

    public String[] getIgnoringArray() {
        String[] whitelistArray = new String[ignoring.size()];
        return ignoring.toArray(whitelistArray);
    }

    public Set<String> getIgnoring() {
        return ignoring;
    }

    @Data
    public static final class UrlAuthConfig {

        private String url;

        private String filter;
    }
}
