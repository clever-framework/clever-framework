package io.github.toquery.framework.core.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface AppUserDetailService extends UserDetailsService {

    UserDetails getById(Long userId);

    UserDetails loadFullUserByUsername(String username) throws UsernameNotFoundException;

    Map<Long, AppUserDetails> userDetailsMap(Set<Long> userIds);
}
