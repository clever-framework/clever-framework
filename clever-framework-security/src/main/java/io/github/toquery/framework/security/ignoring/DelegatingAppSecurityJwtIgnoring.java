package io.github.toquery.framework.security.ignoring;

import com.google.common.collect.Sets;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.core.security.AppSecurityIgnoring;
import io.github.toquery.framework.security.properties.AppSecurityProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 *
 */
@Slf4j
@RequiredArgsConstructor
public class DelegatingAppSecurityJwtIgnoring implements AppSecurityIgnoring {

    private final AppSecurityProperties appSecurityProperties;
    private final List<AppSecurityIgnoring> appSecurityIgnoringList;

    @Override
    public Set<String> ignoring() {
        Set<String> ignoringSet = Sets.newHashSet();
        // 获取框架配置的地址
        if (appSecurityIgnoringList != null && !appSecurityIgnoringList.isEmpty()) {
            ignoringSet.addAll(appSecurityIgnoringList.stream().flatMap(appSecurityIgnoring -> appSecurityIgnoring.ignoring().stream()).collect(Collectors.toSet()));
            log.debug("加载框架白名单URI {} 个 , 分别是 {}", ignoringSet.size(), JacksonUtils.object2String(ignoringSet));
        }
        ignoringSet.addAll(appSecurityProperties.getIgnoring());
        log.debug("加载配置文件白名单URI {} 个 , 分别是 {}", appSecurityProperties.getIgnoring().size(), JacksonUtils.object2String(appSecurityProperties.getIgnoring()));

        return ignoringSet;
    }

    public String[] getIgnoringArray() {
        Set<String> ignoringSet = this.ignoring();
        String[] ignoringArray = new String[ignoringSet.size()];
        return ignoringSet.toArray(ignoringArray);
    }
}
