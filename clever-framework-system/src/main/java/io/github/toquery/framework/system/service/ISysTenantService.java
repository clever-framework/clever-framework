package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.system.entity.SysTenant;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysTenantService extends IService<SysTenant> {

    SysTenant saveSysTenantCheck(SysTenant sysTenant);

    SysTenant updateSysTenantCheck(SysTenant sysTenant);

    SysTenant deleteSysTenantCheck(Set<Long> ids);
}
