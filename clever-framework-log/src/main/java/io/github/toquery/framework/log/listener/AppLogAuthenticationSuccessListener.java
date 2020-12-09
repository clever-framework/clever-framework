package io.github.toquery.framework.log.listener;

import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.log.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;

@Slf4j
public class AppLogAuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    @Autowired
    private ISysLogService sysLogService;

    public AppLogAuthenticationSuccessListener() {
        log.info("初始化 App Log 登录成功监听 {}", this.getClass().getName());
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = event.getAuthentication();
        log.info("用户 {} 成功登录", authentication.getName());
        sysLogService.insertSysLog(((AppUserDetails) authentication.getPrincipal()).getId(), "系统", "登录成功", null, authentication.getName() + " " + "登录成功", null);
    }


}
