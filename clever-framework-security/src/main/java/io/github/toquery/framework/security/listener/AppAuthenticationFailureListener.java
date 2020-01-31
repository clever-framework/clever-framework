package io.github.toquery.framework.security.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;

@Slf4j
public class AppAuthenticationFailureListener implements ApplicationListener<AbstractAuthenticationFailureEvent> {

    @Override
    public void onApplicationEvent(AbstractAuthenticationFailureEvent event) {
        log.info("用户名:{},成功登录", event.getAuthentication().getName());
    }

}
