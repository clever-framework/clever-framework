package io.github.toquery.framework.grpc.client.exception;

/**
 * @since 2019/1/15
 */
public class GRpcChannelCreateException extends RuntimeException{
    public GRpcChannelCreateException(String message) {
        super(message);
    }
}
