package io.github.toquery.framework.security.autoconfig;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import io.github.toquery.framework.security.ignoring.AppSecurityJwtIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.properties.AppSecurityJWTProperties;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import io.github.toquery.framework.security.provider.JwtTokenProvider;
import io.github.toquery.framework.security.utils.AppRSAUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

/**
 * <a href="https://juejin.cn/post/6985893815500406791">https://juejin.cn/post/6985893815500406791</a>
 * 默认
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Import({AppSecurityJwtIgnoring.class})
@EnableConfigurationProperties({AppSecurityProperties.class, AppSecurityJWTProperties.class, AppSecurityAdminProperties.class})
public class AppSecurityJWTAutoConfiguration {

    @Resource
    private AppSecurityProperties appSecurityProperties;


    public AppSecurityJWTAutoConfiguration() {
        log.info("自动装配 App Security JWT 自动化配置, 包括 JwtEncoder、JwtDecoder、、SecurityFilterChain(defaultSecurityFilterChain), JwtTokenProvider");
    }


    @Bean
    @ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, value = "enabled-dje", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public JwtEncoder jwtEncoder() {
        AppSecurityProperties.AppSecurityKey appSecurityKey = appSecurityProperties.getKey();
        JWK jwk = new RSAKey.Builder(AppRSAUtils.publicKey(appSecurityKey.getPublicKey()))
                .privateKey(AppRSAUtils.privateKey(appSecurityKey.getPrivateKey()))
                .keyID(appSecurityKey.getKeyId())
                .build();
        JWKSet jwkSet = new JWKSet(jwk);
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(jwkSet);
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    @ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, value = "enabled-djd", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(AppRSAUtils.publicKey(appSecurityProperties.getKey().getPublicKey()))
                .jwtProcessorCustomizer(jwtProcessorCustomizer -> {
                    JwtValidators.createDefault();
                    JwtValidators.createDefaultWithIssuer(appSecurityProperties.getJwt().getIssuer());
                })
                .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = AppSecurityProperties.PREFIX, value = "enabled-djp", havingValue = "true", matchIfMissing = true)
    @ConditionalOnMissingBean
    public JwtTokenProvider jwtTokenProvider(
            JwtEncoder encoder,
            AppSecurityJWTProperties appSecurityJWTProperties
    ) {
        return new JwtTokenProvider(encoder, appSecurityJWTProperties);
    }


}
