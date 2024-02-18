package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.system.entity.SysConfig;
import io.github.toquery.framework.system.mapper.SysConfigMapper;
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
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements ISysConfigService {

    public static final Set<String> UPDATE_FIELD = Sets.newHashSet("configName", "configValue", "configDesc", "sortNum", "disable");

    /**
     * 查询条件表达式
     */
    public static final Map<String, String> QUERY_EXPRESSIONS = new LinkedHashMap<String, String>() {
        {
            put("configName", "configName:EQ");
            put("configValue", "configValue:EQ");

            put("configNameLike", "configName:LIKE");
            put("configValueLike", "configValue:LIKE");
        }
    };

    @Override
    public List<SysConfig> findByConfigName(String configName) {
        LambdaQueryWrapper<SysConfig> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysConfig::getConfigName, configName);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public SysConfig saveSysConfigCheck(SysConfig sysConfig) {
        List<SysConfig> sysConfigList = this.findByConfigName(sysConfig.getConfigName());
        if (!sysConfigList.isEmpty()) {
            throw new RuntimeException("配置项已存在");
        }
        super.save(sysConfig);
        return sysConfig;
    }

    @Override
    public SysConfig updateSysConfigCheck(SysConfig sysConfig) {
        List<SysConfig> sysConfigList = this.findByConfigName(sysConfig.getConfigName());
        if (sysConfigList.stream().anyMatch(item -> !item.getId().equals(sysConfig.getId()))) {
            throw new RuntimeException("配置项已存在");
        }
        super.updateById(sysConfig);
        return sysConfig;
    }

    @Override
    public SysConfig value(String configName) {
        return this.findByConfigName(configName).stream().findAny().orElse(null);
    }
}
