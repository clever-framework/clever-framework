package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.system.entity.SysConfig;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface ISysConfigService extends IService<SysConfig> {
    List<SysConfig> findByConfigName(String configName);

    SysConfig saveSysConfigCheck(SysConfig sysConfig);

    SysConfig updateSysConfigCheck(SysConfig sysConfig);

    SysConfig value(String configName);
}
