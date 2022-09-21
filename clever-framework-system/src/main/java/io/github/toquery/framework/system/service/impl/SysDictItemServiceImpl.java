package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.mapper.SysDictItemMapper;
import io.github.toquery.framework.system.service.ISysDictItemService;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysDictItemServiceImpl extends ServiceImpl<SysDictItemMapper, SysDictItem> implements ISysDictItemService {

    public static final Set<String> UPDATE_FIELD = Sets.newHashSet("itemText", "itemValue", "itemDesc", "sortNum", "disable");


    @Override
    public List<SysDictItem> findByDictId(Long dictId) {
        LambdaQueryWrapper<SysDictItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysDictItem::getDictId, dictId);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public List<SysDictItem> findByDictIds(Set<Long> dictIds) {
        LambdaQueryWrapper<SysDictItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysDictItem::getDictId, dictIds);
        return super.list(lambdaQueryWrapper);
    }

    @Override
    public void deleteByDictId(Long dictId) {
        LambdaQueryWrapper<SysDictItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysDictItem::getDictId, dictId);
        super.remove(lambdaQueryWrapper);
    }

    @Override
    public void deleteByDictIds(Set<Long> dictIds) {
        LambdaQueryWrapper<SysDictItem> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysDictItem::getDictId, dictIds);
        super.remove(lambdaQueryWrapper);
    }

    @Override
    public List<SysDictItem> reSave(Long dictId, List<SysDictItem> dictItems) {
        // find all item
        List<SysDictItem> dbSysDictItems = findByDictId(dictId);

        List<Long> newSysDictItemIds = dictItems.stream().map(SysDictItem::getId).collect(Collectors.toList());

        List<Long> dbSysDictItemIds = dbSysDictItems.stream().map(SysDictItem::getId).collect(Collectors.toList());

        //
        List<Long> deleteIds = dbSysDictItemIds.stream().filter(id -> !newSysDictItemIds.contains(id)).collect(Collectors.toList());

        List<SysDictItem> updates = dbSysDictItems.stream()
                .filter(dbSysDictItem -> newSysDictItemIds.contains(dbSysDictItem.getId()))
                .peek(dictItem -> dictItem.setDictId(dictId))
                .collect(Collectors.toList());

        List<SysDictItem> saves = dictItems.stream()
                .filter(dictItem -> !dbSysDictItemIds.contains(dictItem.getId()))
                .peek(dictItem -> {
                    dictItem.setDictId(dictId);
                    dictItem.preInsert();
                })
                .collect(Collectors.toList());

        if (deleteIds.size() > 0) {
            super.removeBatchByIds(deleteIds);
        }

        if (updates.size() > 0) {
            super.updateBatchById(updates);
        }

        if (saves.size() > 0) {
            super.saveBatch(saves);
        }

        return Lists.newArrayList(Iterables.concat(updates, saves));
    }

    @Override
    public SysDictItem saveSysDictCheck(SysDictItem sysDictItem) {
        super.save(sysDictItem);
        return sysDictItem;
    }

    @Override
    public SysDictItem updateSysDictItemCheck(SysDictItem sysDictItem) {
        super.updateById(sysDictItem);
        return sysDictItem;
    }
}
