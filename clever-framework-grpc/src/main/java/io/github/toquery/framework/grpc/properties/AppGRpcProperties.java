package io.github.toquery.framework.grpc.properties;

import io.github.toquery.framework.grpc.client.model.ChannelProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @since 2019/1/15
 */
@ConfigurationProperties(prefix = "app.grpc")
@Data
public class AppGRpcProperties {
    /**
     * 是否开启grpc：true/false
     */
    private boolean enabled;
    /**
     * 序列化工具
     */
    private MarshallerType marshaller = MarshallerType.JSON;
    /**
     * 客户端配置
     */
    private Client client = new Client();
    /**
     * 服务端配置
     */
    private Server server = new Server();

    @ConfigurationProperties(prefix = "app.grpc.client")
    @Data
    public static class Client {
        /**
         * 是否开启客户端：true/false
         */
        private boolean enabled;
        /**
         * 服务列表
         */
        private Map<String, ChannelProperty> services = new HashMap<>();
        /**
         * 是否开启代理
         */
        private boolean proxyEnable = false;
    }

    @ConfigurationProperties(prefix = "app.grpc.server")
    @Data
    public static class Server {

        /**
         * 是否开启服务端：true/false
         */
        private boolean enabled;

        /**
         * 端口号
         */
        private int port = 50051;
    }

    public enum MarshallerType {
        PROTO,
        JSON
    }
}
