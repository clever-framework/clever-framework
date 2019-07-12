package io.github.toquery.framework.system.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.curd.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.domain.SysMenu;
import io.github.toquery.framework.system.repository.SysMenuRepository;
import io.github.toquery.framework.system.service.ISysMenuService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysMenuServiceImpl extends AppBaseServiceImpl<Long, SysMenu, SysMenuRepository> implements ISysMenuService {
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("id", "id:EQ");
            put("idIN", "id:IN");
            put("name", "name:EQ");
            put("code", "code:EQ");
            put("parentId", "parentId:EQ");
            put("parentIdIN", "parentId:IN");
            put("parentIdsIN", "parentIds:IN");
            put("level", "level:EQ");
            put("levelIN", "level:IN");
            put("parentPath", "parentPath:EQ");
            put("leaf", "leaf:EQ");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
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

    @Override
    public List<SysMenu> findByParentId(Long parentId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("parentId", parentId);
        return this.find(params);
    }


    @Override
    public List<SysMenu> findByParentIds(Set<Long> parentIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("parentIdIN", parentIds);
        return this.find(params);
    }

    /**
     *
     * @param ids 删除
     */
    @Override
    public void deleteMenu(Set<Long> ids) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("idIN", ids);
        List<SysMenu> sysMenuList = this.find(params);

        List<SysMenu> parentSysMenuList = this.findByParentIds(sysMenuList.stream().map(SysMenu::getParentId).collect(Collectors.toSet()));

        // 同级兄弟id
        Map<Long, List<SysMenu>> brotherIdMap = parentSysMenuList.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        for (SysMenu sysMenu : sysMenuList) {
            if (!sysMenu.getHasChildren()) {
                // 同级菜单信息
                List<SysMenu> brotherSysMenu = brotherIdMap.get(sysMenu.getParentId());
                if (brotherSysMenu != null && brotherSysMenu.size() == 1){
                    SysMenu parentSysMenu = getById(sysMenu.getParentId());
                    parentSysMenu.setHasChildren(false);
                    this.update(parentSysMenu,Sets.newHashSet("hasChildren"));
                }
            }
            this.deleteById(sysMenu.getId());
        }
    }
}
