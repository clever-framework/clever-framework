package io.github.toquery.framework.security.model;


import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * jwt 响应实体
 */
@Data
@AllArgsConstructor
public class JwtResponse {

    private String token;
}
