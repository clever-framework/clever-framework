package io.github.toquery.framework.grpc.server.adapter;

import io.github.toquery.framework.grpc.server.configure.GRpcServerBuilderConfigure;
import io.grpc.ServerBuilder;

import java.util.List;

/**
 * @since 2019/1/17
 */
public class DefaultServerBuilderConfigureAdapter extends GRpcServerBuilderConfigure {

    public DefaultServerBuilderConfigureAdapter(List<BindServiceAdapter> bindServiceAdapterList, int port) {
        super(bindServiceAdapterList, port);
    }

    @Override
    public void configure(ServerBuilder serverBuilder) {
    }
}
