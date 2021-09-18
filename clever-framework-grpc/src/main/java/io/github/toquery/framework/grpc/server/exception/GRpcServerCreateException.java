package io.github.toquery.framework.grpc.server.exception;

/**
 * @since 2019/1/15
 */
public class GRpcServerCreateException extends RuntimeException{
    public GRpcServerCreateException(String message) {
        super(message);
    }
}
