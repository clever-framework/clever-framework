package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface ISysConfigService extends AppBaseService<SysConfig, Long> {
    List<SysConfig> reSave(Long bizId, String configGroup, List<SysConfig> sysConfigList);
}
