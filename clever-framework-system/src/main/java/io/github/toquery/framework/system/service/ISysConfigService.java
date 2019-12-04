package io.github.toquery.framework.system.service;

import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Service
public interface ISysConfigService extends AppBaseService<SysConfig, Long> {
    List<SysConfig> reSave(Long bizId, String configGroup, List<SysConfig> sysConfigList);
}
