package io.github.toquery.framework.dao.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Properties;

@Slf4j
@Intercepts(@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}))
public class AppEntityAuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws IllegalAccessException, InvocationTargetException {
        fillField(invocation);
        return invocation.proceed();
    }

    private void fillField(Invocation invocation) {
        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];

            //第一个参数处理。根据它判断是否给“操作属性”赋值。
            if (arg instanceof MappedStatement) {//如果是第一个参数 MappedStatement
                MappedStatement ms = (MappedStatement) arg;
                sqlCommandType = ms.getSqlCommandType();
                log.debug("操作类型：" + sqlCommandType);
                if (sqlCommandType == SqlCommandType.INSERT || sqlCommandType == SqlCommandType.UPDATE) {//如果是“增加”或“更新”操作，则继续进行默认操作信息赋值。否则，则退出
                    continue;
                } else {
                    break;
                }
            }

            if (sqlCommandType == SqlCommandType.INSERT) {
                for (Field f : arg.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    switch (f.getName()) {
                        case "createUserId":
                            setProperty(arg, "createUserId", "111");
                            break;
                        case "createDatetime":
                            setProperty(arg, "createDatetime", new Date());
                            break;
                        case "lastUpdateUserId":
                            setProperty(arg, "lastUpdateUserId", "111");
                            break;
                        case "lastUpdateDatetime":
                            setProperty(arg, "lastUpdateDatetime", new Date());
                            break;
                        case "delFlag":
                            setProperty(arg, "delFlag", "0");
                            break;
                    }
                }
            } else if (sqlCommandType == SqlCommandType.UPDATE) {
                for (Field f : arg.getClass().getDeclaredFields()) {
                    f.setAccessible(true);
                    switch (f.getName()) {
                        case "lastUpdateUserId":
                            setProperty(arg, "lastUpdateUserId", "111");
                            break;
                        case "lastUpdateDatetime":
                            setProperty(arg, "lastUpdateDatetime", new Date());
                            break;
                    }
                }
            }
        }
    }

    /**
     * 为对象的操作属性赋值
     *
     * @param bean
     */
    private void setProperty(Object bean, String name, Object value) {
        try {
            //根据需要，将相关属性赋上默认值
            BeanUtils.setProperty(bean, name, value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object plugin(Object object) {
        return Plugin.wrap(object, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
