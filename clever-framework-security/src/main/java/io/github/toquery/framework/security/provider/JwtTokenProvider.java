package io.github.toquery.framework.security.provider;

import com.google.common.collect.Lists;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import java.time.Instant;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {


    private final JwtEncoder encoder;
    private final AppSecurityJwtProperties appSecurityJwtProperties;


    public String issueToken(String userId, String device) {
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(appSecurityJwtProperties.getExpires());

        JwtClaimsSet claims = JwtClaimsSet.builder()
                // .id(userId)
                .issuer(appSecurityJwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expires)
                .subject(userId)
                .audience(Lists.newArrayList(device))
                // .claim(AppSecurityKey.SCOPE, scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
