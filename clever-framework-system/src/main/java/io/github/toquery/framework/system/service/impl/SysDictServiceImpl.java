package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.mapper.SysDictMapper;
import io.github.toquery.framework.system.service.ISysDictItemService;
import io.github.toquery.framework.system.service.ISysDictService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {


    @Autowired
    private ISysDictItemService sysDictItemService;

    @Override
    public List<SysDict> findByCode(String dictCode) {
        LambdaQueryWrapper<SysDict> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysDict::getDictCode, dictCode);
        return super.list(lambdaQueryWrapper);
    }


    @Override
    public SysDict getByCode(String dictCode) {
        List<SysDict> sysDicts = this.findByCode(dictCode);
        if (sysDicts == null || sysDicts.size() <= 0) {
            throw new AppException("未找到字典项" + dictCode);
        }
        return sysDicts.get(0);
    }

    @Override
    public List<SysDictItem> findDictItemByDictCode(String dictCode) {
        SysDict sysDict = this.getByCode(dictCode);
        return sysDictItemService.findByDictId(sysDict.getId());
    }

    @Override
    public SysDict getWithItemById(Long id) {
        SysDict sysDict = super.getById(id);
        List<SysDictItem> sysDictItems = sysDictItemService.findByDictId(sysDict.getId());
        sysDict.setDictItems(sysDictItems);
        return sysDict;
    }

    @Override
    public SysDict updateSysDictCheck(SysDict sysDict) {
        Long sysDictId = sysDict.getId();
        List<SysDict> sysDicts = this.findByCode(sysDict.getDictCode());
        if (sysDicts != null && sysDicts.size() > 1 && sysDicts.stream().noneMatch(dbSysDict -> dbSysDict.getId().equals(sysDictId))) {
            throw new AppException("已存在字典项" + sysDict.getDictCode());
        }

        LambdaUpdateWrapper<SysDict> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysDict::getId, sysDict.getId());
        lambdaUpdateWrapper.set(SysDict::getDictName, sysDict.getDictName());
        lambdaUpdateWrapper.set(SysDict::getDictCode, sysDict.getDictCode());
        lambdaUpdateWrapper.set(SysDict::getDictDesc, sysDict.getDictDesc());
        lambdaUpdateWrapper.set(SysDict::getSortNum, sysDict.getSortNum());

        super.update(sysDict, lambdaUpdateWrapper);

        if (sysDict.getDictItems() != null && sysDict.getDictItems().size() > 0) {
            List<SysDictItem> sysDictItems = sysDictItemService.reSave(sysDict.getId(), sysDict.getDictItems());
            sysDict.setDictItems(sysDictItems);
        }
        return sysDict;
    }

    @Override
    public SysDictItem item(String dictCode, String itemValue) {
        List<SysDictItem> sysDictItems = this.findDictItemByDictCode(dictCode);
        if (sysDictItems == null || sysDictItems.size() <= 0) {
            throw new AppException("未找到字典项" + dictCode);
        }
        Optional<SysDictItem> optional = sysDictItems.stream().filter(item -> item.getItemValue().equalsIgnoreCase(itemValue)).findAny();
        if (!optional.isPresent()) {
            throw new AppException("未找到字典项" + dictCode + itemValue);
        }
        return optional.get();
    }

    @Override
    public void deleteSysDictCheck(Set<Long> ids) {
        sysDictItemService.deleteByDictIds(ids);
        super.removeByIds(ids);
    }

    @Override
    public SysDict saveSysDictCheck(SysDict sysDict) {
        List<SysDict> sysDicts = this.findByCode(sysDict.getDictCode());
        if (sysDicts != null && sysDicts.size() > 1) {
            throw new AppException("已存在字典项 " + sysDict.getDictCode());
        }
        super.save(sysDict);
        List<SysDictItem> dictItems = sysDict.getDictItems();
        if (dictItems != null && dictItems.size() > 0) {
            List<SysDictItem> sysDictItems = sysDictItemService.reSave(sysDict.getId(), dictItems);
            sysDict.setDictItems(sysDictItems);
        }
        return sysDict;
    }
}
