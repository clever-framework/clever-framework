package io.github.toquery.framework.security.jwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Date;
import java.util.function.Function;

@Slf4j
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 通过 token 获取 username
     *
     * @param token token
     * @return username
     */
    public static String getUsernameFromToken(String signingKey, String token) {
        return getClaimFromToken(signingKey, token, Claims::getSubject);
    }

    public static Date getIssuedAtDateFromToken(String signingKey, String token) {
        return getClaimFromToken(signingKey, token, Claims::getIssuedAt);
    }

    public static Date getExpirationDateFromToken(String signingKey, String token) {
        return getClaimFromToken(signingKey, token, Claims::getExpiration);
    }

    /**
     * 解析 token，获取相应属性
     *
     * @param token          token
     * @param claimsResolver 获取相应属性
     * @param <T>            获取相应属性
     * @return 获取相应属性
     */
    public static <T> T getClaimFromToken(String signingKey, String token, Function<Claims, T> claimsResolver) {
        Claims claims = getAllClaimsFromToken(signingKey, token);
        return claimsResolver.apply(claims);
    }

    /**
     * 解析 token 对象获取实体
     *
     * @param token 实体
     * @return 实体
     */
    public static Claims getAllClaimsFromToken(String signingKey, String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(token)
                .getBody();
    }

    public static Boolean isTokenExpired(String signingKey, String token) {
        Date expiration = getExpirationDateFromToken(signingKey, token);
        return expiration.before(new Date());
    }

    /**
     * 检测token是否在修改密码之前
     *
     * @param created           token创建时间
     * @param lastPasswordReset 最后修改密码时间
     * @return true 创建token在修改密码之前，false 创建token在修改密码之后
     */
    public static Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return (lastPasswordReset != null && created.before(lastPasswordReset));
    }
}
