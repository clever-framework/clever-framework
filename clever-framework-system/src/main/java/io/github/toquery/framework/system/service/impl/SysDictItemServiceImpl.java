package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.repository.SysDictItemRepository;
import io.github.toquery.framework.system.service.ISysDictItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysDictItemServiceImpl extends AppBaseServiceImpl<SysDictItem, SysDictItemRepository>
        implements ISysDictItemService {

    public static final Set<String> UPDATE_FIELD = Sets.newHashSet("itemText", "itemValue", "itemDesc", "sortNum", "disable");

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("idIN", "id:IN");
        map.put("dictId", "dictId:EQ");
        map.put("dictIds", "dictId:IN");
        map.put("codeLike", "code:LIKE");
        return map;
    }

    @Override
    public List<SysDictItem> findByDictId(Long dictId) {
        Map<String, Object> param = new HashMap<>();
        param.put("dictId", dictId);
        return super.find(param);
    }

    @Override
    public List<SysDictItem> findByDictIds(Set<Long> dictIds) {
        Map<String, Object> param = new HashMap<>();
        param.put("dictIds", dictIds);
        return super.find(param);
    }

    @Override
    public void deleteByDictId(Long dictId) {
        Map<String, Object> param = new HashMap<>();
        param.put("dictId", dictId);
        super.delete(param);
    }

    @Override
    public void deleteByDictIds(Set<Long> dictIds) {
        Map<String, Object> param = new HashMap<>();
        param.put("dictIds", dictIds);
        super.delete(param);
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
            super.deleteByIds(deleteIds);
        }

        if (updates.size() > 0) {
            super.update(updates, UPDATE_FIELD);
        }

        if (saves.size() > 0) {
            super.save(saves);
        }

        return Lists.newArrayList(Iterables.concat(updates, saves));
    }
}
