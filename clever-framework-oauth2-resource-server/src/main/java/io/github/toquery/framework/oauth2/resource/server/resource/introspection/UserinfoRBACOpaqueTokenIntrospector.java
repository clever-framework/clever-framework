package io.github.toquery.framework.oauth2.resource.server.resource.introspection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toquery.framework.oauth2.resource.server.properties.AppOAuth2ResourceServerOpaqueTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 增加用户详细信息和RBAC模型
 */
@Slf4j
public class UserinfoRBACOpaqueTokenIntrospector extends UserinfoOpaqueTokenIntrospector {

    private final ObjectMapper objectMapper;

    public UserinfoRBACOpaqueTokenIntrospector(
            ObjectMapper objectMapper,
            AppOAuth2ResourceServerOpaqueTokenProperties opaqueTokenProperties
    ) {
        super(opaqueTokenProperties);
        this.objectMapper = objectMapper;
    }

    @Override
    public String cachePrefix(String key) {
        return "user_info_rbac:" + key;
    }

    @Override
    protected Map<String, Object> expandAttributes(String token, Map<String, Object> attributes) {
        return super.expandAttributes(token, attributes);
    }

    @Override
    protected Collection<? extends GrantedAuthority> expandAuthorities(String token, Collection<? extends GrantedAuthority> originalAuthorities) {
        Collection<GrantedAuthority> authorities = new ArrayList<>(super.expandAuthorities(token, originalAuthorities));
        if (opaqueTokenProperties.isRbacEnabled()) {
            Collection<SimpleGrantedAuthority> rbacs = makeRBACRequest(token)
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            authorities.addAll(rbacs);

        }
        return authorities;
    }


    /**
     * 请求获取用户RBAC信息
     *
     * @param bearerToken
     */
    private Collection<String> makeRBACRequest(String bearerToken) {
        log.debug("开始向认证服务拉取用户RBAC信息");
        WebClient.RequestHeadersSpec<?> webClientBuilder = webClient
                .get()
                .uri(opaqueTokenProperties.getUserInfoUri());

        String rbacStr = this.headersSpec(bearerToken, webClientBuilder)
                .retrieve()
                .bodyToMono(String.class)
                .block(opaqueTokenProperties.getRbacTimeout());
        if (rbacStr == null) {
            throw new RuntimeException("获取用户RBAC信息失败");
        }
        log.debug("拉取用户RBAC信息结束");

        Collection<String> rbacs = new ArrayList<>();

        try (
                JsonParser jsonParser = objectMapper.createParser(rbacStr)
        ) {

            String rbacCodePath = opaqueTokenProperties.getRbacCodePath();
            // ObjectMapper读取json文件
            JsonNode root = objectMapper.readTree(rbacStr);

            if (rbacCodePath == null && jsonParser.isExpectedStartArrayToken()) { // 如果 rbacCodePath 不为空 并且 返回为数组，则直接是 RBAC code
                rbacs = objectMapper.readValue(rbacStr, new TypeReference<ArrayList<String>>() {
                });
            } else if (rbacCodePath != null && jsonParser.isExpectedStartObjectToken()) { // 如果 rbacCodePath 不为空 并且 返回为对象，则是嵌套 RBAC code

                if (rbacCodePath.contains(".")) {
                    String[] paths = rbacCodePath.split("\\.");
                    for (int i = 0; i < paths.length; i++) {
                        root = root.get(paths[i]);
                        if (i == paths.length - 1) {
                            rbacs = root.findValuesAsText(rbacCodePath);
                        }
                    }
                } else {
                    rbacs = root.findValuesAsText(rbacCodePath);
                }
            } else {
                log.error("解析远程RBAC错误， 接收到的JSON数据为 {}", rbacStr);
                throw new RuntimeException("解析远程RBAC错误");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return rbacs;
    }

    protected WebClient.RequestHeadersSpec<?> headersSpec(String bearerToken, WebClient.RequestHeadersSpec<?> webClientBuilder) {
        return webClientBuilder.headers(h -> h.setBearerAuth(bearerToken))
                .headers(httpHeaders -> httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON)));
    }
}
