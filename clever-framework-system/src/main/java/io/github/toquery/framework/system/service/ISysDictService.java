package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.entity.SysDictItem;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface ISysDictService extends AppBaseService<SysDict> {

    List<SysDict> findByCode(String dictCode);

    SysDict getByCode(String dictCode);

    List<SysDictItem> findDictItemByDictCode(String dictCode);

    SysDict getWithItemById(Long id);

    SysDict updateSysDictCheck(SysDict sysDict);

    SysDictItem item(String dictCode, String itemValue);
}
