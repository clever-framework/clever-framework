package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Maps;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysConfig;
import io.github.toquery.framework.system.repository.SysConfigRepository;
import io.github.toquery.framework.system.service.ISysConfigService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysConfigServiceImpl extends AppBaseServiceImpl<SysConfig, SysConfigRepository> implements ISysConfigService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("bizId", "bizId:EQ");
            put("configGroup", "configGroup:EQ");
            put("configName", "configName:EQ");
            put("configValue", "configValue:EQ");

            put("configGroupLike", "configGroup:LIKE");
            put("configNameLike", "configName:LIKE");
            put("configValueLike", "configValue:LIKE");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    @Override
    public List<SysConfig> reSave(Long bizId, String configGroup, List<SysConfig> sysConfigList) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("configGroup", configGroup);
        if (bizId != null) {
            params.put("bizId", bizId);
        }
        List<SysConfig> dbSysConfig = this.find(params);
        this.deleteByIds(dbSysConfig.stream().map(SysConfig::getId).collect(Collectors.toSet()));
        return this.save(sysConfigList);
    }
}
