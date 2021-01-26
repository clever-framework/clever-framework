package io.github.toquery.framework.system.service;

import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysDict;
import io.github.toquery.framework.system.entity.SysDictItem;

import java.util.List;

/**
 * @author toquery
 * @version 1
 */
public interface ISysDictItemService extends AppBaseService<SysDictItem, Long> {

    List<SysDictItem> findByDictId(Long dictId);
}
