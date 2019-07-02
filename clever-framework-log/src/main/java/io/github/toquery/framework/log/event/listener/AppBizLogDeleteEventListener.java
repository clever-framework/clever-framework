package io.github.toquery.framework.log.event.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.DeleteEvent;
import org.hibernate.event.spi.DeleteEventListener;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppBizLogDeleteEventListener implements DeleteEventListener {

    @Override
    public void onDelete(DeleteEvent event) throws HibernateException {

    }

    @Override
    public void onDelete(DeleteEvent event, Set transientEntities) throws HibernateException {
        log.debug("接收到 transientEntities 数据。\n {}", JSON.toJSONString(transientEntities));
        this.onDelete(event);
    }
}
