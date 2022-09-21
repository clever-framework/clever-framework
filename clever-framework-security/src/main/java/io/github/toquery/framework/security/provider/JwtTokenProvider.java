package io.github.toquery.framework.security.provider;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.system.entity.SysUser;
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

    public String issueToken(SysUser sysUser, String device) {

        return this.issueToken(sysUser, null, device);
    }

    public String issueToken(SysUser sysUser, String tokenId, String device) {

        Instant now = Instant.now();
        Instant expires = now.plusSeconds(appSecurityJwtProperties.getExpires());

        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder();
        if (Strings.isNullOrEmpty(tokenId)) {
            claimsBuilder.id(tokenId);
        }

        claimsBuilder
                .issuer(appSecurityJwtProperties.getIssuer())
                .issuedAt(now)
                .expiresAt(expires)
                .subject(sysUser.getUsername())
                .audience(Lists.newArrayList(device))
                // .claim(AppSecurityKey.SCOPE, scope)
                ;

        return this.encoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
    }

}
