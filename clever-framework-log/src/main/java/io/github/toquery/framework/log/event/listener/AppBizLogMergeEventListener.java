package io.github.toquery.framework.log.event.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.event.spi.MergeEvent;
import org.hibernate.event.spi.MergeEventListener;

import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppBizLogMergeEventListener implements MergeEventListener {
    @Override
    public void onMerge(MergeEvent event) throws HibernateException {

    }

    @Override
    public void onMerge(MergeEvent event, Map copiedAlready) throws HibernateException {
        log.debug("接收到 copiedAlready 数据。\n {}", JSON.toJSONString(copiedAlready));
        this.onMerge(event);
    }
}
