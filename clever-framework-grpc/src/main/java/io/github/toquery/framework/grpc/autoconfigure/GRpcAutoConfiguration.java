package io.github.toquery.framework.grpc.autoconfigure;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.framework.grpc.client.annotation.GRpcClientScan;
import io.github.toquery.framework.grpc.client.factory.ClientFactory;
import io.github.toquery.framework.grpc.client.proxy.CloseProxyDetector;
import io.github.toquery.framework.grpc.core.exception.CreateMarshallerException;
import io.github.toquery.framework.grpc.core.factory.JacksonMarshallerFactory;
import io.github.toquery.framework.grpc.core.factory.MarshallerFactory;
import io.github.toquery.framework.grpc.core.factory.ProtoMarshallerFactory;
import io.github.toquery.framework.grpc.properties.AppGRpcProperties;
import io.github.toquery.framework.grpc.server.adapter.DefaultServerBuilderConfigureAdapter;
import io.github.toquery.framework.grpc.server.annotation.GRpcServerScan;
import io.github.toquery.framework.grpc.server.configure.GRpcServerBuilderConfigure;
import io.github.toquery.framework.grpc.server.processor.GRpcServiceProcessor;
import io.github.toquery.framework.grpc.server.run.GRpcServerApplicationRunner;
import io.grpc.ProxyDetector;
import io.grpc.internal.GrpcUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @since 2019/1/15
 */
@Configuration
@EnableConfigurationProperties({AppGRpcProperties.class, AppGRpcProperties.Client.class, AppGRpcProperties.Server.class})
@ConditionalOnProperty(prefix = "app.grpc", name = "enabled", havingValue = "true", matchIfMissing = true)
@Import({GRpcAutoConfiguration.GrpcClientAutoConfiguration.class, GRpcAutoConfiguration.GrpcServerAutoConfiguration.class})
public class GRpcAutoConfiguration {

    /**
     * @param objectMapper jackson操作工具类
     * @return jackson序列化工厂
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "app.grpc", name = "marshaller", havingValue = "json", matchIfMissing = true)
    public MarshallerFactory jsonMarshallerFactory(ObjectMapper objectMapper) {
        if (objectMapper == null) {
            throw new CreateMarshallerException("Object mapper is no inject in spring.Please check your configuration.");
        }
        return new JacksonMarshallerFactory(objectMapper);
    }

    /**
     * @return protobuf 序列化工厂
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "app.grpc", name = "marshaller", havingValue = "proto")
    public MarshallerFactory protoMarshallerFactory() {
        return new ProtoMarshallerFactory();
    }

    /**
     * 客户端配置
     */
    @ConditionalOnProperty(prefix = "app.grpc.client", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GRpcClientScan
    public static class GrpcClientAutoConfiguration {
        @Autowired
        private AppGRpcProperties grpcProperties;


        @Bean
        @ConditionalOnMissingBean
        public ClientFactory clientFactory(MarshallerFactory marshallerFactory) {
            ProxyDetector proxyDetector = grpcProperties.getClient().isProxyEnable() ? GrpcUtil.DEFAULT_PROXY_DETECTOR : new CloseProxyDetector();
            ClientFactory clientFactory = new ClientFactory(marshallerFactory, proxyDetector);
            clientFactory.setServerChannelMap(grpcProperties.getClient().getServices());
            return clientFactory;
        }
    }

    /**
     * 服务端配置
     */
    @ConditionalOnProperty(prefix = "app.grpc.server", name = "enabled", havingValue = "true", matchIfMissing = true)
    @GRpcServerScan
    public static class GrpcServerAutoConfiguration {
        @Autowired
        private AppGRpcProperties grpcProperties;

        /**
         * server builder 配置
         *
         * @param grpcServiceProcessor grpcServiceProcessor
         * @return server builder 配置
         */
        @Bean
        @ConditionalOnMissingBean
        public GRpcServerBuilderConfigure grpcServerBuilderConfigure(GRpcServiceProcessor grpcServiceProcessor) {
            return new DefaultServerBuilderConfigureAdapter(grpcServiceProcessor.getBindServiceAdapterList(), grpcProperties.getServer().getPort());
        }

        /**
         * 扫描grpc server 服务
         *
         * @param marshallerFactory 序列化处理器
         * @return GRpcServiceProcessor
         */
        @Bean
        @ConditionalOnMissingBean
        public GRpcServiceProcessor grpcServiceProcessor(MarshallerFactory marshallerFactory) {
            return new GRpcServiceProcessor(marshallerFactory);
        }

        /**
         * grpc 服务
         *
         * @param grpcServerBuilderConfigure grpcServerBuilderConfigure
         * @return 服务
         */
        @Bean
        @ConditionalOnMissingBean
        public GRpcServerApplicationRunner grpcServerApplicationRunner(GRpcServerBuilderConfigure grpcServerBuilderConfigure) {
            return new GRpcServerApplicationRunner(grpcServerBuilderConfigure);
        }
    }
}
