package io.github.toquery.framework.system.service.impl;

import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.repository.SysDictRepository;
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
public class SysDictServiceImpl extends AppBaseServiceImpl<SysDict, SysDictRepository> implements ISysDictService {


    @Autowired
    private ISysDictItemService sysDictItemService;

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("idIN", "id:IN");
        map.put("dictCode", "dictCode:LIKE");
        map.put("dictName", "dictName:LIKE");
        map.put("description", "description:LIKE");
        return map;
    }

    @Override
    public List<SysDict> findByCode(String dictCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("dictCode", dictCode);
        return super.list(param);
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
        List<SysDictItem> sysDictItems = sysDictItemService.reSave(sysDict.getId(), sysDict.getDictItems());
        sysDict = super.update(sysDict, Sets.newHashSet("dictName", "dictCode", "description", "sortNum"));
        sysDict.setDictItems(sysDictItems);
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
        super.deleteByIds(ids);
    }

    @Override
    public SysDict saveSysDictCheck(SysDict sysDict) {
        List<SysDict> sysDicts = this.findByCode(sysDict.getDictCode());
        if (sysDicts != null && sysDicts.size() > 1) {
            throw new AppException("已存在字典项 " + sysDict.getDictCode());
        }
        super.save(sysDict);
        List<SysDictItem> sysDictItems = sysDictItemService.reSave(sysDict.getId(), sysDict.getDictItems());
        sysDict.setDictItems(sysDictItems);
        return sysDict;
    }
}
