package io.github.toquery.framework.log.listener;

import io.github.toquery.framework.log.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

@Slf4j
public class AppLogAuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Autowired
    private ISysLogService sysLogService;

    public AppLogAuthenticationFailureListener() {
        log.info("初始化 App Log 登录失败监听 {}", this.getClass().getName());
    }

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        String username = event.getAuthentication().getName();
        String message = event.getException().getMessage();
        log.info("用户 {} 登录失败 {}", username, message);
        sysLogService.insertSysLog(null, "系统", "登录失败", null, username + " " + message, null);
    }

}
