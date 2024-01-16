package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.system.entity.SysTenant;
import io.github.toquery.framework.system.mapper.SysTenantMapper;
import io.github.toquery.framework.system.service.ISysTenantService;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@RequiredArgsConstructor
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {
    @Override
    public SysTenant saveSysTenantCheck(SysTenant sysTenant) {
        long count = super.count(new LambdaQueryWrapper<SysTenant>().eq(SysTenant::getTenantCode,sysTenant.getTenantCode()));
        if (count > 0){
            throw new AppException("已存在改租户编码");
        }
        super.save(sysTenant);
        return sysTenant;
    }

    @Override
    public SysTenant updateSysTenantCheck(SysTenant sysTenant) {
        SysTenant dbSysTenant = super.getById(sysTenant.getId());
        if (!dbSysTenant.getTenantCode().equals(sysTenant.getTenantCode())){
            throw new AppException("不允许修改租户编码");
        }
        super.updateById(sysTenant);
        return sysTenant;
    }

    @Override
    public SysTenant deleteSysTenantCheck(Set<Long> ids) {
        if (!ids.isEmpty()) {
            throw new AppException("不允许修改租户编码");
        }
        return null;
    }
}
