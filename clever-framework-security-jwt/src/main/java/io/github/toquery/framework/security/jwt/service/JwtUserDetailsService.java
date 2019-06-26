package io.github.toquery.framework.security.jwt.service;

import io.github.toquery.framework.security.system.domain.SysUser;
import io.github.toquery.framework.security.system.repository.SysUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class JwtUserDetailsService implements UserDetailsService, JwtUserRegister {

    @Resource
    private SysUserRepository sysUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return user;
        }
    }

    @Override
    public SysUser register(SysUser user) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        user.setPassword(encoder.encode(user.getPassword()));
//        user.setAuthorities(Lists.newArrayList(new Authority(AuthorityName.ROLE_ADMIN),new Authority(AuthorityName.ROLE_USER)));
        return sysUserRepository.save(user);
    }
}
