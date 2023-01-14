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

/**
 * 增加用户详细信息
 */
@Slf4j
public class UserinfoOpaqueTokenIntrospector implements OpaqueTokenIntrospector {

    private final Cache<String, OAuth2AuthenticatedPrincipal> userInfoCache;

    private final OpaqueTokenIntrospector delegate;
    protected final AppOAuth2ResourceServerOpaqueTokenProperties opaqueTokenProperties;

    protected final WebClient webClient;


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

    public String cachePrefix(String key) {
        return "user_info:" + key;
    }

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        // 查找缓存，如果缓存不存在则生成缓存元素,  如果无法生成则返回null
        return userInfoCache.get(cachePrefix(token), value -> {
            log.debug("开始向认证服务验证 token {}", token);
            OAuth2AuthenticatedPrincipal authorized = this.delegate.introspect(token);
            log.debug("认证服务验证结束 token {}", token);
            // 获取用户信息
            Map<String, Object> userInfo = makeUserInfoRequest(token);
            // 将用户信息转化为当前 Principal
            Map<String, Object> attributes = convertUserInfoAttributes(userInfo, authorized.getAttributes());
            // 拓展
            return convertOAuth2AuthenticatedPrincipal(expandAttributes(token, attributes), expandAuthorities(token, authorized.getAuthorities()));
        });
    }

    protected Map<String, Object> expandAttributes(String token, Map<String, Object> attributes) {
        return attributes;
    }

    protected Collection<? extends GrantedAuthority> expandAuthorities(String token, Collection<? extends GrantedAuthority> originalAuthorities) {
        return originalAuthorities;
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

    private OAuth2AuthenticatedPrincipal convertOAuth2AuthenticatedPrincipal(
            Map<String, Object> attributes,
            Collection<? extends GrantedAuthority> originalAuthorities
    ) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        if (originalAuthorities != null) {
            authorities.addAll(originalAuthorities);
        }
        return new OAuth2IntrospectionAuthenticatedPrincipal(Collections.unmodifiableMap(attributes), authorities);
    }

    private Map<String, Object> convertUserInfoAttributes(Map<String, Object> userInfo, Map<String, Object> originalAttributes) {
        Map<String, Object> attributes = new HashMap<>(originalAttributes);
        opaqueTokenProperties.getUserInfoAttributes().forEach(userInfoAttribute -> {
            if (attributes.containsKey(userInfoAttribute)) {
                log.warn("当前已存在用户属性 {} 将不会覆盖", userInfoAttribute);
            } else {
                attributes.put(userInfoAttribute, userInfo.get(userInfoAttribute));
            }
        });

        return attributes;
    }
}
