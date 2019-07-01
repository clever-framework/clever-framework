package io.github.toquery.framework.dao.audit;

import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import javax.annotation.Resource;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
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
        log.info("初始化App Entity DDD 监听器 - Spring DDD Model ");
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

    @PrePersist
    public void onPrePersist(AppBaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener ---- @PrePersist");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPrePersist(appBaseEntity));
    }

    /**
     * todo BUG，修改数据时不经过这个
     */
    @PreUpdate
    public void onPreUpdate(AppBaseEntity appBaseEntity) {
        log.debug("Spring DDD Model: AppEntityD3Listener - @PreUpdate");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPreUpdate(appBaseEntity));
    }

    @PreRemove
    public void onPreRemove(AppBaseEntity appBaseEntity) {
        log.info("Spring DDD Model: AppEntityD3Listener - @PreRemove");
        appAuditorHandles.stream().filter(AppAuditorHandler::enable).forEach(item -> item.onPreRemove(appBaseEntity));
    }

}
