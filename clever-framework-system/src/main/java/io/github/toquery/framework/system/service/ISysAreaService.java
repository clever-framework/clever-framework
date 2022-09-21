package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.system.entity.SysArea;

/**
 * @author toquery
 * @version 1
 */
public interface ISysAreaService extends IService<SysArea> {

    SysArea update(SysArea sysArea);
}
