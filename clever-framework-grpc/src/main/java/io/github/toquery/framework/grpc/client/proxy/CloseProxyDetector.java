package io.github.toquery.framework.grpc.client.proxy;

import io.grpc.ProxiedSocketAddress;
import io.grpc.ProxyDetector;

import javax.annotation.Nullable;
import java.io.IOException;
import java.net.SocketAddress;

/**
 * @since 2021-04-22 10:21
 */
public class CloseProxyDetector implements ProxyDetector {
    @Nullable
    @Override
    public ProxiedSocketAddress proxyFor(SocketAddress targetServerAddress) throws IOException {
        return null;
    }
}
