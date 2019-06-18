package io.github.toquery.framework.security.system.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.security.system.domain.SysMenu;
import io.github.toquery.framework.security.system.repository.SysMenuRepository;
import io.github.toquery.framework.security.system.service.ISysMenuService;
import io.github.toquery.framework.security.utils.UtilTree;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Service
public class SysMenuServiceImpl extends AppBaseServiceImpl<Long, SysMenu, SysMenuRepository> implements ISysMenuService {

    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("name", "name:EQ");
            put("code", "code:EQ");
            put("parentId", "parentId:EQ");
            put("level", "level:EQ");
            put("levelIn", "level:IN");
            put("parentPath", "parentPath:EQ");
            put("leaf", "leaf:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    @Override
    public List<SysMenu> findTree() {
        List<SysMenu> sysMenuList = this.find(Maps.newHashMap(),new String[]{"sortNum_desc"});
        return UtilTree.getTreeData(sysMenuList);
    }

    @Override
    public List<SysMenu> permission() {
        Map<String, Object> params = Maps.newHashMap();
        params.put("levelIn", Sets.newHashSet(1, 2));
        List<SysMenu> sysMenuList = this.find(params);
        Map<Integer, List<SysMenu>> temp = sysMenuList.stream().collect(Collectors.groupingBy(SysMenu::getLevel));

        Map<Long, List<SysMenu>> parentMap = temp.get(2).stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        return temp.get(1).stream().map(key -> {
            key.setChildren(parentMap.get(key.getId()));
            return key;
        }).collect(Collectors.toList());
    }

    @Override
    public SysMenu saveMenu(SysMenu sysMenu) {
        SysMenu parentSysMenu = this.getById(sysMenu.getParentId());

        parentSysMenu.setHasChildren(true);
        this.update(parentSysMenu, Sets.newHashSet("hasChildren"));

        sysMenu.setHasChildren(false);
        sysMenu.setLevel(parentSysMenu.getLevel() + 1);
        if (Strings.isNullOrEmpty(parentSysMenu.getParentIds())) {
            sysMenu.setParentIds(parentSysMenu.getId().toString());
        } else {
            sysMenu.setParentIds(parentSysMenu.getParentIds() + "," + parentSysMenu.getId());
        }

        return this.save(sysMenu);
    }
}
