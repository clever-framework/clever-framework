package io.github.toquery.framework.security.jwt.service;


import io.github.toquery.framework.security.jwt.security.User;

/**
 * @author toquery
 * @version 1
 */
public interface JwtUserRegister {
    public User register(User user);
}
