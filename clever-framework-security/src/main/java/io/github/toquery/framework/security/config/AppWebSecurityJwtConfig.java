package io.github.toquery.framework.security.config;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityJwtProperties;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
public class AppWebSecurityJwtConfig implements AppSecurityIgnoring {

    public AppWebSecurityJwtConfig() {
        log.info("初始化 App Web Security Jwt 配置");
    }

    @Resource
    private AppSecurityJwtProperties appSecurityJwtProperties;

    @Override
    public Set<String> ignoring() {
        AppSecurityJwtProperties.AppJwtPathProperties pathProperties = appSecurityJwtProperties.getPath();
        return Sets.newHashSet(pathProperties.getRegister(), pathProperties.getToken());
    }

}
