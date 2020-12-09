package io.github.toquery.framework.security.jwt.handler;

import com.google.common.base.Strings;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.security.jwt.exception.AppSecurityJwtException;
import io.github.toquery.framework.security.jwt.properties.AppSecurityJwtProperties;
import io.github.toquery.framework.security.jwt.utils.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JwtTokenHandler {
    public static final String PREFIX_AUTHORIZATION = "Bearer ";

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AppSecurityJwtProperties appSecurityJwtProperties;


    public JwtTokenHandler() {
        log.info("初始化 JwtTokenHandler ");
    }


    public String getJwtToken() {
        return this.getJwtToken(request);
    }

    public String getJwtToken(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Strings.isNullOrEmpty(token)) {
            log.warn("处理认证请求 {} 时未从 header 获取 {} 将从请求param中读取。", request.getRequestURL(), HttpHeaders.AUTHORIZATION);
            String[] requestParam = request.getParameterValues(HttpHeaders.AUTHORIZATION);
            if (requestParam != null && requestParam.length > 0 && !Strings.isNullOrEmpty(requestParam[0])) {
                token = requestParam[0];
            }
        }

        if (Strings.isNullOrEmpty(token)){
            log.error("未获取到 Token 信息");
            throw new AppSecurityJwtException(HttpStatus.BAD_REQUEST, "未获取到 Token 信息");
//            return null;
        }

        if ((token.startsWith(PREFIX_AUTHORIZATION) || token.startsWith(PREFIX_AUTHORIZATION.toLowerCase()) || token.startsWith(PREFIX_AUTHORIZATION.toUpperCase()))) {
            token = token.substring(PREFIX_AUTHORIZATION.length());
        }

        return token;
    }

    public Claims getClaims() {
        return this.getClaimsFromToken(this.getJwtToken());
    }

    public Claims getClaimsFromToken(String token) {
        return JwtTokenUtil.getClaimsFromToken(appSecurityJwtProperties.getSecret(), token);
    }

    public Date getExpirationDateFromToken(String token) {
        return JwtTokenUtil.getExpirationDateFromToken(appSecurityJwtProperties.getSecret(), token);
    }


    public String getUsernameFromToken(String token) {
        return JwtTokenUtil.getUsernameFromToken(appSecurityJwtProperties.getSecret(), token);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, userDetails.getUsername(), appSecurityJwtProperties.getSecret());
    }

    public String generateToken(UserDetails userDetails, String signingKey) {
        Map<String, Object> claims = new HashMap<>();
        return this.doGenerateToken(claims, userDetails.getUsername(), signingKey);
    }

    public String doGenerateToken(Map<String, Object> claims, String subject, String signingKey) {
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

        Claims claims = JwtTokenUtil.getClaimsFromToken(appSecurityJwtProperties.getSecret(), token);
        claims.setIssuedAt(createdDate);
        claims.setExpiration(expirationDate);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, appSecurityJwtProperties.getSecret())
                .compact();
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return appSecurityJwtProperties.isIgnoreTokenExpires();
    }

    /**
     * 判断JWT令牌是否过期
     */
    public boolean isValid(String token) {
        Date expirationDate = JwtTokenUtil.getExpirationDateFromToken(appSecurityJwtProperties.getSecret(), token);
        return expirationDate != null && expirationDate.after(new Date());
    }

    /**
     * 判断是否需要生成新的授权
     */
    public boolean needRenewal(String token) {
        Date expiresAt = this.getExpirationDateFromToken(token);
        expiresAt.setTime(expiresAt.getTime() + appSecurityJwtProperties.getRenewalAheadTime());
        return !expiresAt.after(new Date()); //是否需要提前刷新JWT令牌?
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
        return new Date(createdDate.getTime() + appSecurityJwtProperties.getExpires() * 1000);
    }
}
