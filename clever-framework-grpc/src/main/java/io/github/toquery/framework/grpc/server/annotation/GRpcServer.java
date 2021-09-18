package io.github.toquery.framework.grpc.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * grpc服务端组件。被{@link GRpcServerScan}扫描注入到spring中
 *
 * @since 2019/1/13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface GRpcServer {
    /**
     * @return 提供的scheme名称, client端调用时需统一。
     */
    String value();
}
