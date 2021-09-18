package io.github.toquery.framework.grpc.client.model;

import lombok.Data;

/**
 * @since 2019/1/15
 */
@Data
public class ChannelProperty {
    private String host;
    private int port = 50051;
}
