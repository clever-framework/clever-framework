package io.github.toquery.framework.oauth2.resource.server.resource.introspection;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.google.common.base.Strings;
import io.github.toquery.framework.oauth2.resource.server.properties.AppOAuth2ResourceServerOpaqueTokenProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

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
            try {
                Collection<SimpleGrantedAuthority> rbacs = makeRBACRequest(token)
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();
                authorities.addAll(rbacs);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return authorities;
    }


    /**
     * 请求获取用户RBAC信息
     *
     * @param bearerToken
     */
    private Collection<String> makeRBACRequest(String bearerToken) throws Exception {
        log.debug("开始向认证服务拉取用户RBAC信息");
        Map<String, String> params = new HashMap<String, String>();
        params.put("clientId", opaqueTokenProperties.getRbacClientId());
        WebClient.RequestHeadersSpec<?> webClientBuilder = webClient
                .get()
                .uri(opaqueTokenProperties.getRbacUri() + "?clientId={clientId}", params);

        String rbacStr = this.headersSpec(bearerToken, webClientBuilder)
                .retrieve()
                .bodyToMono(String.class)
                .block(opaqueTokenProperties.getRbacTimeout());
        if (rbacStr == null) {
            throw new RuntimeException("获取用户RBAC信息失败");
        }
        log.debug("拉取用户RBAC信息结束");

        Collection<String> rbacs = new ArrayList<>();


        String rbacCodePrefix = opaqueTokenProperties.getRbacCodePrefix();
        String rbacCodeKey = opaqueTokenProperties.getRbacCodePrefix();
        // ObjectMapper读取json文件
        JsonNode root = objectMapper.readTree(rbacStr);

        if (Strings.isNullOrEmpty(rbacCodePrefix)) {
            if (Strings.isNullOrEmpty(rbacCodeKey)) {
                if (root.getNodeType() == JsonNodeType.ARRAY) { // 如果 rbacCodePrefix 为空 rbacCodeKey 为空 并且 返回为数组，则直接是 RBAC code
                    rbacs = StreamSupport.stream(Spliterators.spliteratorUnknownSize(root.elements(), 0), false).map(JsonNode::asText).toList();
                } else {
                    throw new RuntimeException("接收到的JSON格式不正确 " + rbacStr);
                }

            } else {
                if (root.getNodeType() == JsonNodeType.ARRAY) { // 如果 rbacCodePrefix 为空 rbacCodeKey 不为空 并且 则读取key再去获取list RBAC code
                    rbacs = root.findValuesAsText(rbacCodeKey);
                } else {
                    throw new RuntimeException("接收到的JSON格式不正确 " + rbacStr);
                }
            }
        } else {
            JsonNode listNode = root;
            String[] paths = rbacCodePrefix.split("\\.");
            for (String path : paths) {
                listNode = listNode.get(path);
            }
            if (Strings.isNullOrEmpty(rbacCodeKey)) {
                if (listNode.getNodeType() == JsonNodeType.ARRAY) { // 如果 rbacCodePrefix 为空 rbacCodeKey 为空 并且 返回为数组，则直接是 RBAC code
                    rbacs = StreamSupport.stream(Spliterators.spliteratorUnknownSize(listNode.elements(), 0), false).map(JsonNode::asText).toList();
                } else {
                    throw new RuntimeException("接收到的JSON格式不正确 " + rbacStr);
                }
            } else {
                rbacs = root.findValuesAsText(rbacCodeKey);
            }
        }
        return rbacs;
    }

    protected WebClient.RequestHeadersSpec<?> headersSpec(String bearerToken, WebClient.RequestHeadersSpec<?> webClientBuilder) {
        return webClientBuilder.headers(h -> h.setBearerAuth(bearerToken))
                .headers(httpHeaders -> httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON)));
    }
}
