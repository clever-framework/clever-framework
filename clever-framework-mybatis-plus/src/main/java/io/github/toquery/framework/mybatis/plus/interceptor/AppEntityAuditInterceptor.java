package io.github.toquery.framework.mybatis.plus.interceptor;

import io.github.toquery.framework.core.entity.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.util.ArrayList;
import java.util.Properties;

@Slf4j
@Intercepts({

//        @Signature(
//                type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
//        ),
//        @Signature(
//                type = Executor.class,
//                method = "query",
//                args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}
//        ),

//        @Signature(
//                type = Executor.class,
//                method = "insert",
//                args = {MappedStatement.class, Object.class}
//        ),

        @Signature(
                type = Executor.class,
                method = "update",
                args = {MappedStatement.class, Object.class}
        )
})
public class AppEntityAuditInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        SqlCommandType sqlCommandType = null;


        for (Object object : args) {
            // 从MappedStatement参数中获取到操作类型
            if (object instanceof MappedStatement) {
                MappedStatement ms = (MappedStatement) object;
                sqlCommandType = ms.getSqlCommandType();
                log.debug("操作类型： {}", sqlCommandType);
                continue;
            }
            // 判断参数是否是 BaseEntity 类型
            // 一个参数
            if (object instanceof BaseEntity) {
                BaseEntity appBaseEntity = (BaseEntity) object;
                if (SqlCommandType.INSERT == sqlCommandType) {
                    appBaseEntity.preInsert();
                    continue;
                }
                if (SqlCommandType.UPDATE == sqlCommandType) {
                    appBaseEntity.preUpdate();
                    continue;
                }
            }

            // 兼容MyBatis的updateByExampleSelective(record, example);
            if (object instanceof MapperMethod.ParamMap) {
                log.debug("mybatis arg: {}", object);
                MapperMethod.ParamMap<Object> parasMap = (MapperMethod.ParamMap<Object>) object;
                String key = "record";
                if (!parasMap.containsKey(key)) {
                    continue;
                }
                Object paraObject = parasMap.get(key);
                if (paraObject instanceof BaseEntity) {
                    BaseEntity appBaseEntity = (BaseEntity) paraObject;
                    if (SqlCommandType.UPDATE == sqlCommandType) {
                        appBaseEntity.preUpdate();
                        continue;
                    }
                }
            }
            // 兼容批量插入
            if (object instanceof DefaultSqlSession.StrictMap) {
                log.debug("mybatis arg: {}", object);
                DefaultSqlSession.StrictMap<ArrayList<Object>> map = (DefaultSqlSession.StrictMap<ArrayList<Object>>) object;
                String key = "collection";
                if (!map.containsKey(key)) {
                    continue;
                }
                ArrayList<Object> objs = map.get(key);
                for (Object obj : objs) {
                    if (obj instanceof BaseEntity) {
                        BaseEntity appBaseEntity = (BaseEntity) obj;
                        if (SqlCommandType.INSERT == sqlCommandType) {
                            appBaseEntity.preInsert();
                        }
                        if (SqlCommandType.UPDATE == sqlCommandType) {
                            appBaseEntity.preInsert();
                        }
                    }
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        Interceptor.super.setProperties(properties);
    }
}
