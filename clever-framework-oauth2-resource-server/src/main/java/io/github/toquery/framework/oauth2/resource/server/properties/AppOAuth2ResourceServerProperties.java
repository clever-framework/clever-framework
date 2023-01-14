package io.github.toquery.framework.oauth2.resource.server.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

/**
 *
 */
@Setter
@Getter
public class AppOAuth2ResourceServerProperties extends OAuth2ResourceServerProperties {

    private final AppOAuth2ResourceServerOpaqueTokenProperties opaqueToken = new AppOAuth2ResourceServerOpaqueTokenProperties();

}
