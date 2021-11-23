package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysConfig;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface ISysConfigService extends AppBaseService<SysConfig> {
    List<SysConfig> reSave(Long bizId, String configGroup, List<SysConfig> sysConfigList);

    List<SysConfig> findByConfigName(String configName);

    SysConfig saveSysConfigCheck(SysConfig sysConfig);

    SysConfig updateSysConfigCheck(SysConfig sysConfig);

    SysConfig value(String configName);
}
