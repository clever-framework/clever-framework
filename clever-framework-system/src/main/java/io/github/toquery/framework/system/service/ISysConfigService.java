package io.github.toquery.framework.system.service;

import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.system.domain.SysConfig;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface ISysConfigService extends AppBaseService<SysConfig, Long> {
    List<SysConfig> reSave(Long bizId, String configGroup, List<SysConfig> sysConfigList);
}
