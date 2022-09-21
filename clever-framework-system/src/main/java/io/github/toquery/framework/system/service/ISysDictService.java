package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.entity.SysDictItem;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysDictService extends IService<SysDict> {

    List<SysDict> findByCode(String dictCode);

    SysDict getByCode(String dictCode);

    List<SysDictItem> findDictItemByDictCode(String dictCode);

    SysDict getWithItemById(Long id);

    SysDict updateSysDictCheck(SysDict sysDict);

    SysDictItem item(String dictCode, String itemValue);

    void deleteSysDictCheck(Set<Long> ids);

    SysDict saveSysDictCheck(SysDict sysDict);
}
