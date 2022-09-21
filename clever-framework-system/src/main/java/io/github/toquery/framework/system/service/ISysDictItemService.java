package io.github.toquery.framework.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.github.toquery.framework.system.entity.SysDictItem;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysDictItemService extends IService<SysDictItem> {

    List<SysDictItem> findByDictId(Long dictId);

    List<SysDictItem> findByDictIds(Set<Long> dictIds);

    void deleteByDictId(Long dictId);

    void deleteByDictIds(Set<Long> dictIds);

    List<SysDictItem> reSave(Long dictId, List<SysDictItem> dictItems);


    SysDictItem saveSysDictCheck(SysDictItem sysDictItem);

    SysDictItem updateSysDictItemCheck(SysDictItem sysDictItem);

}
