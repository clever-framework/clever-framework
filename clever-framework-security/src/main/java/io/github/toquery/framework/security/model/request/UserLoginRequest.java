package io.github.toquery.framework.security.model.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 */
@Setter
@Getter
@Builder
@EqualsAndHashCode
@ToString
public class UserLoginRequest {

    private String username;
    private String password;
}
