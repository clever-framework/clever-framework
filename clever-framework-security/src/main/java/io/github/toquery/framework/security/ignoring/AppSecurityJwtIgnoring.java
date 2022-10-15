package io.github.toquery.framework.security.ignoring;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityAdminProperties;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.Set;

@Slf4j
public class AppSecurityJwtIgnoring implements AppSecurityIgnoring {

    public AppSecurityJwtIgnoring() {
        log.info("初始化 App Web Security Jwt 配置");
    }

    @Resource
    private AppSecurityAdminProperties appSecurityJwtProperties;

    @Override
    public Set<String> ignoring() {
        AppSecurityAdminProperties.AppSecurityAdminPathProperties pathProperties = appSecurityJwtProperties.getPath();
        return Sets.newHashSet(pathProperties.getRegister(), pathProperties.getToken());
    }

}
