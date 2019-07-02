package com.toquery.framework.demo.audit;

import io.github.toquery.framework.dao.audit.AppAuditorHandler;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@Component
public class MyAudit implements AppAuditorHandler {

    public MyAudit() {
        log.debug("123");
    }

    @Override
    public boolean enable() {
        return false;
    }

    @Override
    public void onPrePersist(AppBaseEntity appBaseEntity) {
        log.debug("123");
    }

    @Override
    public void onPreUpdate(AppBaseEntity appBaseEntity) {
        log.debug("123");
    }

    @Override
    public void onPreRemove(AppBaseEntity appBaseEntity) {
        log.debug("123");
    }

    @Override
    public void onPostLoad(AppBaseEntity appBaseEntity) {

    }

    @Override
    public void onPostPersist(AppBaseEntity appBaseEntity) {

    }

    @Override
    public void onPostUpdate(AppBaseEntity appBaseEntity) {

    }

    @Override
    public void onPostRemove(AppBaseEntity appBaseEntity) {

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
