package io.github.toquery.framework.jpa.audit;

import io.github.toquery.framework.core.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import jakarta.annotation.Resource;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import java.util.HashSet;
import java.util.Set;

/**
 * Spring DDD驱动监听器
 * <p>
 * InitializingBean, ApplicationContextAware  在这里实现这几个接口并不起效
 *
 * @author toquery
 * @version 1
 */
@Slf4j
@Scope("singleton")
public class AppEntityD3Listener {


    @Resource
    private Set<AppAuditorHandler> appAuditorHandles = new HashSet<>();

    public AppEntityD3Listener() {
        log.debug("初始化App Entity DDD 监听器 - Spring DDD Model ");
    }

    /**
     * 必须存在于实体父类中
     */
    @DomainEvents
    public void domainEvents() {
        log.debug("Spring DDD Model: AppEntityD3Listener ---- @DomainEvents");
    }

    /**
     * 必须存在于实体父类中
     */
    @AfterDomainEventPublication
    public void afterDomainEventPublication() {
        log.debug("Spring DDD Model: AppEntityD3Listener ---- @AfterDomainEventPublication");
    }

    @PostLoad
    public void onPostLoad(BaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener -- {} -- @PostLoad", appBaseEntity.getClass().getSimpleName());
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPostLoad(appBaseEntity));
    }

    @PrePersist
    public void onPrePersist(BaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener ---- @PrePersist");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPrePersist(appBaseEntity));
    }

    @PostPersist
    public void onPostPersist(BaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener ---- @PostPersist");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPostPersist(appBaseEntity));
    }


    /**
     * todo BUG，修改数据时不经过这个
     */
    @PreUpdate
    public void onPreUpdate(BaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener - @PreUpdate");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPreUpdate(appBaseEntity));
    }

    @PostUpdate
    public void onPostUpdate(BaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener ---- @PostUpdate");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPostUpdate(appBaseEntity));

    }

    @PreRemove
    public void onPreRemove(BaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener - @PreRemove");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPreRemove(appBaseEntity));
    }

    @PostRemove
    public void onPostRemove(BaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener ---- @PostRemove");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPostRemove(appBaseEntity));

    }

}
