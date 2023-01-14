package io.github.toquery.framework.oauth2.resource.server.resource.introspection;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.toquery.framework.oauth2.resource.server.properties.AppOAuth2ResourceServerOpaqueTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Slf4j
public class UserinfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final Cache<String, OAuth2AuthenticatedPrincipal> userInfoCache;


    private final OpaqueTokenIntrospector delegate;
    private final AppOAuth2ResourceServerOpaqueTokenProperties opaqueTokenProperties;

    private final WebClient webClient;


    public UserinfoOpaqueTokenIntrospector(
            AppOAuth2ResourceServerOpaqueTokenProperties opaqueTokenProperties
    ) {
        ClientHttpConnector clientHttpConnector = new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection()));
        webClient = WebClient.builder()
                .clientConnector(clientHttpConnector)
                .build();

        this.opaqueTokenProperties = opaqueTokenProperties;
        this.delegate = new SpringOpaqueTokenIntrospector(opaqueTokenProperties.getIntrospectionUri(), opaqueTokenProperties.getClientId(), opaqueTokenProperties.getClientSecret());


        this.userInfoCache = Caffeine.newBuilder() //本地缓存
                .maximumSize(5000)
                .expireAfterAccess(opaqueTokenProperties.getCacheExpired())
                .build();
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        return userInfoCache.get(token, value -> {
            log.debug("开始向认证服务验证 token {}", token);
            OAuth2AuthenticatedPrincipal authorized = this.delegate.introspect(token);
            log.debug("认证服务验证结束 token {}", token);
            Map<String, Object> userInfo = makeUserInfoRequest(token);
            return convertOAuth2AuthenticatedPrincipal(userInfo, authorized);
        });
    }

    /**
     * 请求获取用户信息
     *
     * @param bearerToken
     */
    private Map<String, Object> makeUserInfoRequest(String bearerToken) {
        log.debug("开始向认证服务拉取用户信息");
        Map<String, Object> userInfo = webClient
                .get()
                .uri(opaqueTokenProperties.getUserInfoUri())
                .headers(h -> h.setBearerAuth(bearerToken))
                .headers(httpHeaders -> httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON)))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block(opaqueTokenProperties.getUserInfoTimeout());
        if (userInfo == null) {
            throw new RuntimeException("获取用户信息失败");
        }
        log.debug("拉取用户信息结束");

        return userInfo;
    }

    private OAuth2AuthenticatedPrincipal convertOAuth2AuthenticatedPrincipal(Map<String, Object> userInfo, OAuth2AuthenticatedPrincipal authorized) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (authorized.getAuthorities() != null) {
            authorities.addAll(authorized.getAuthorities());
        }

        Map<String, Object> attributes = new HashMap<>(authorized.getAttributes());
        opaqueTokenProperties.getUserInfoAttributes().forEach(userInfoAttribute -> {
            if (attributes.containsKey(userInfoAttribute)) {
                log.warn("当前已存在用户属性 {} 将不会覆盖", userInfoAttribute);
            } else {
                attributes.put(userInfoAttribute, userInfo.get(userInfoAttribute));
            }
        });

        return new OAuth2IntrospectionAuthenticatedPrincipal(Collections.unmodifiableMap(attributes), authorities);
    }
}
