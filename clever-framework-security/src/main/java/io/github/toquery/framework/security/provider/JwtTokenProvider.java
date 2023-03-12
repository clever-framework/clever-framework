package io.github.toquery.framework.security.provider;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.toquery.framework.common.util.AppNanoIdUtils;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import io.github.toquery.framework.security.properties.AppSecurityJWTProperties;
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
    private final AppSecurityJWTProperties appSecurityJWTProperties;

    public String issueToken(String username, String device) {
        return this.issueToken(username, AppNanoIdUtils.randomNanoId(), device);
    }

    public String issueToken(String username, String tokenId, String device) {

        Instant now = Instant.now();
        Instant expires = now.plusSeconds(appSecurityJWTProperties.getExpires().toSeconds());

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
        if (!Strings.isNullOrEmpty(tokenId)) {
            claimsBuilder.id(tokenId);
        }

        claimsBuilder
                .issuer(appSecurityJWTProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expires)
                .subject(username)
                .audience(Lists.newArrayList(device))
                // .claim(AppSecurityKey.SCOPE, scope)
                ;

        return this.encoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
    }

}
