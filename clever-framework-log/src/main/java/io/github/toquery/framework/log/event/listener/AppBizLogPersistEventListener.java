package io.github.toquery.framework.log.event.listener;

import com.alibaba.fastjson.JSON;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;

import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppBizLogPersistEventListener implements PersistEventListener {

    public static final AppBizLogPersistEventListener INSTANCE = new AppBizLogPersistEventListener();

    @Override
    public void onPersist(PersistEvent event) throws HibernateException {
        Object entity = event.getObject();
        if (entity instanceof AppBaseEntity) {
            log.info("Incrementing {} entity version because a {} child entity has been inserted", "", entity);
        } else {
            log.warn("123");
        }
    }

    @Override
    public void onPersist(PersistEvent event, Map createdAlready) throws HibernateException {
        log.debug("接收到 createdAlready 数据。\n {}", JSON.toJSONString(createdAlready));
        this.onPersist(event);
    }
}