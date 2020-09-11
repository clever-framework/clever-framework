package io.github.toquery.framework.security.jwt.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

/**
 * JWT令牌
 *
 */
@Setter
@Getter
@ToString
public class JwtToken {

	/**
	 * JWT令牌值
	 */
	@JsonIgnore
	private String value;

	/**
	 * JWT令牌的来源(请求header/cookie)
	 */
	@JsonProperty("source")
	private TokenSource source;

	/**
	 * 用户名
	 */
	@JsonProperty("username")
	private String username;

	/**
	 * Token下发时间
	 */
	@JsonProperty("iat")
	private Instant issuedAt;

	/**
	 * Token过期时间
	 */
	@JsonProperty("exp")
	private Instant expiresAt;

	/**
	 * 判断JWT令牌是否过期
	 */
	public boolean isValid() {
		return expiresAt != null && expiresAt.isAfter(Instant.now());
	}

}
