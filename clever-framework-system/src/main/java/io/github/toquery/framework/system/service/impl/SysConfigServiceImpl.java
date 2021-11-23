package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysConfig;
import io.github.toquery.framework.system.repository.SysConfigRepository;
import io.github.toquery.framework.system.service.ISysConfigService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysConfigServiceImpl extends AppBaseServiceImpl<SysConfig, SysConfigRepository> implements ISysConfigService {

    public static final Set<String> UPDATE_FIELD = Sets.newHashSet("configName", "configValue", "configDesc", "sortNum", "disable");

    /**
     * 查询条件表达式
     */
    private static final Map<String, String> QUERY_EXPRESSIONS = new LinkedHashMap<>() {
        {
            put("configName", "configName:EQ");
            put("configValue", "configValue:EQ");

            put("configNameLike", "configName:LIKE");
            put("configValueLike", "configValue:LIKE");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return QUERY_EXPRESSIONS;
    }

    @Override
    public List<SysConfig> reSave(Long bizId, String configGroup, List<SysConfig> sysConfigList) {
        Map<String, Object> params = Maps.newHashMap();

        List<SysConfig> dbSysConfig = this.find(params);
        this.deleteByIds(dbSysConfig.stream().map(SysConfig::getId).collect(Collectors.toSet()));
        return this.save(sysConfigList);
    }

    @Override
    public List<SysConfig> findByConfigName(String configName) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("configName", configName);
        return super.find(params);
    }

    @Override
    public SysConfig saveSysConfigCheck(SysConfig sysConfig) {
        List<SysConfig> sysConfigList = this.findByConfigName(sysConfig.getConfigName());
        if (sysConfigList.size() > 0) {
            throw new RuntimeException("配置项已存在");
        }
        return super.save(sysConfig);
    }

    @Override
    public SysConfig updateSysConfigCheck(SysConfig sysConfig) {
        List<SysConfig> sysConfigList = this.findByConfigName(sysConfig.getConfigName());
        if (sysConfigList.stream().anyMatch(item -> !item.getId().equals(sysConfig.getId()))) {
            throw new RuntimeException("配置项已存在");
        }
        return super.update(sysConfig, UPDATE_FIELD);
    }

    @Override
    public SysConfig value(String configName) {
        return this.findByConfigName(configName).stream().findAny().orElse(null);
    }
}
