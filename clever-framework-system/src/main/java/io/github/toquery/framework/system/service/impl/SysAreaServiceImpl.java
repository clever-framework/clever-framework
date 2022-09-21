package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.toquery.framework.system.entity.SysArea;
import io.github.toquery.framework.system.mapper.SysAreaMapper;
import io.github.toquery.framework.system.service.ISysAreaService;

/**
 * @author toquery
 * @version 1
 */
public class SysAreaServiceImpl extends ServiceImpl<SysAreaMapper, SysArea> implements ISysAreaService {

    @Override
    public SysArea update(SysArea sysArea) {
        LambdaUpdateWrapper<SysArea> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysArea::getId, sysArea.getId());
        lambdaUpdateWrapper.set(SysArea::getAreaName, sysArea.getAreaName());
        lambdaUpdateWrapper.set(SysArea::getAreaCode, sysArea.getAreaCode());
        super.update(sysArea, lambdaUpdateWrapper);
        return sysArea;
    }
}
