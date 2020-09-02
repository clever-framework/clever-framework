package io.github.toquery.framework.core.security;


import org.springframework.security.core.userdetails.UserDetails;

/**
 */
public interface AppUserDetailService {

    UserDetails getById(Long userId);

}
