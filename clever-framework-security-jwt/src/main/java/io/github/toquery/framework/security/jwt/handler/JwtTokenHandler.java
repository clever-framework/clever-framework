package io.github.toquery.framework.security.jwt.handler;

import io.github.toquery.framework.core.security.AppUserDetails;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.jwt.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenHandler {

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;


    public JwtTokenHandler() {
        log.info("初始化 JwtTokenHandler ");
    }



    public String getUsernameFromToken(String token) {
        return JwtTokenUtil.getUsernameFromToken(appSecurityJwtProperties.getSecret(), token);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, userDetails.getUsername(), appSecurityJwtProperties.getSecret());
    }

    public  String generateToken(UserDetails userDetails, String signingKey) {
        Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, userDetails.getUsername(), signingKey);
    }

    public  String doGenerateToken(Map<String, Object> claims, String subject, String signingKey) {
        Date createdDate = new Date();
        Date expirationDate = calculateExpirationDate(createdDate);

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    public boolean canTokenBeRefreshed(String token, Date lastPasswordResetDate) {
        Date created = JwtTokenUtil.getIssuedAtDateFromToken(appSecurityJwtProperties.getSecret(), token);
        return !JwtTokenUtil.isCreatedBeforeLastPasswordReset(created, lastPasswordResetDate)
                && (!JwtTokenUtil.isTokenExpired(appSecurityJwtProperties.getSecret(), token) || ignoreTokenExpiration(token));
    }

    public String refreshToken(String token) {
        Date createdDate = new Date();
        Date expirationDate = calculateExpirationDate(createdDate);

        Claims claims = JwtTokenUtil.getAllClaimsFromToken(appSecurityJwtProperties.getSecret(), token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, appSecurityJwtProperties.getSecret())
                .compact();
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return appSecurityJwtProperties.isIgnoreTokenExpiration();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        AppUserDetails user = (AppUserDetails) userDetails;
        String username = JwtTokenUtil.getUsernameFromToken(appSecurityJwtProperties.getSecret(), token);
        Date created = JwtTokenUtil.getIssuedAtDateFromToken(appSecurityJwtProperties.getSecret(), token);
        //final Date expiration = getExpirationDateFromToken(token);
        return (
                username.equals(user.getUsername())
                        && !JwtTokenUtil.isTokenExpired(appSecurityJwtProperties.getSecret(), token)
                        && !JwtTokenUtil.isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())
        );
    }

    /**
     * 在当前时间增加过期时间
     *
     * @param createdDate 当前时间
     * @return 当前时间+过期时间
     */
    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + appSecurityJwtProperties.getExpiration() * 1000);
    }
}
