package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysDictItem;
import io.github.toquery.framework.system.repository.SysDictItemRepository;
import io.github.toquery.framework.system.service.ISysDictItemService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class SysDictItemServiceImpl extends AppBaseServiceImpl<Long, SysDictItem, SysDictItemRepository> implements ISysDictItemService {

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        map.put("idIN", "id:IN");
        map.put("name", "name:EQ");
        map.put("nameLike", "name:LIKE");

        map.put("code", "code:EQ");
        map.put("codeLike", "code:LIKE");
        return map;
    }


    @Override
    public List<SysDictItem> findByDictId(Long dictId) {
        Map<String, Object> param = new HashMap<>();
        return super.find(param);
    }
}
