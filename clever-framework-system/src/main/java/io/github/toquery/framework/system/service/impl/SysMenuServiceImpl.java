package io.github.toquery.framework.system.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.repository.SysMenuRepository;
import io.github.toquery.framework.system.service.ISysMenuService;
import io.github.toquery.framework.webmvc.domain.ResponsePage;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysMenuServiceImpl extends AppBaseServiceImpl<SysMenu, SysMenuRepository> implements ISysMenuService {


    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<>() {
        {
            put("id", "id:EQ");
            put("idIN", "id:IN");
            put("menuName", "menuName:LIKE");
            put("menuCode", "menuCode:LIKE");
            put("parentId", "parentId:EQ");
            put("parentIdLike", "parentId:LIKE");
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
        List<SysMenu> sysMenuList = this.findByMenuCode(sysMenu.getMenuCode());

        if (sysMenuList != null && sysMenuList.size() > 0){
            throw new AppException("已存在菜单'" + sysMenu.getMenuName() + "'");
        }

        Long parentId = sysMenu.getParentId();

        SysMenu parentSysMenu = parentId != 0L ? this.getById(parentId) : new SysMenu(0L, "根菜单", "root");

        if(parentId != 0L) {
            parentSysMenu.setHasChildren(true);
            this.update(parentSysMenu, Sets.newHashSet("hasChildren"));
        }

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
    public SysMenu updateMenu(SysMenu sysMenu) {

        SysMenu dbSysMenu = super.getById(sysMenu.getId());

        // 是否需要重建树
        if (!dbSysMenu.getParentId().equals(sysMenu.getParentId())) {
            this.rebuildSysMenuChildrenTree(sysMenu, dbSysMenu);
        }

        // 修改当前节点
        Long parentId = sysMenu.getParentId();
        if (parentId == 0L) {
            sysMenu.setMenuLevel(1);
            sysMenu.setParentIds(ROOT_ID);
            sysMenu.setTreePath(ROOT_ID + "," + sysMenu.getId());
        } else {
            SysMenu newParentSysMenu = super.getById(parentId);
            sysMenu.setMenuLevel(newParentSysMenu.getLevel() + 1);
            sysMenu.setParentIds(newParentSysMenu.getParentIds() + "," + newParentSysMenu.getId());
            sysMenu.setTreePath(newParentSysMenu.getTreePath() + "," + sysMenu.getId());
        }

        super.update(sysMenu, UPDATE_FIELD);

        return sysMenu;
    }

    /**
     * 重建子集树
     */
    public void rebuildSysMenuChildrenTree(SysMenu newSysMenu, SysMenu oldSysMenu) throws AppException {

        List<SysMenu> updateSysMenu = Lists.newArrayList();

        // 获取所有旧机构的子级list
        List<SysMenu> oldSysMenuChildrenList = this.findAllChildren(oldSysMenu.getId());
        if (oldSysMenuChildrenList == null || oldSysMenuChildrenList.size() <= 0) {
            return;
        }

        // 处理新父节点信息
        String newParentIds;
        Long newParentId = newSysMenu.getParentId();

        if (newParentId == 0L) {
            newParentIds = ROOT_ID;
        } else {
            SysMenu newParentSysMenu = super.getById(newParentId);
            if (newParentSysMenu == null) {
                throw new AppException("未获取到父级组织信息");
            }
            newParentIds = newParentSysMenu.getParentIds() + "," + newParentSysMenu.getId();

            newParentSysMenu.setHasChildren(true);
            updateSysMenu.add(newParentSysMenu);
        }

        // 旧父级ids前缀
        String oldParentIdsPrefix = oldSysMenu.getParentIds() + "," + oldSysMenu.getId();

        // 新父级ids前缀
        String newParentIdsPrefix = newParentIds + "," + newSysMenu.getId();

        oldSysMenuChildrenList = oldSysMenuChildrenList.stream().map(childrenSysMenu -> {
            String oldParentIds = childrenSysMenu.getParentIds();
            String oldTreePath = childrenSysMenu.getTreePath();

            String newSaveParentIds = oldParentIds.replace(oldParentIdsPrefix, newParentIdsPrefix);
            String newTreePath = oldTreePath.replace(oldParentIdsPrefix, newParentIdsPrefix);

            childrenSysMenu.setParentIds(newSaveParentIds);
            childrenSysMenu.setTreePath(newTreePath);

            childrenSysMenu.setMenuLevel(newSaveParentIds.contains(",") ? newSaveParentIds.split(",").length : 1);

            return childrenSysMenu;
        }).collect(Collectors.toList());

        updateSysMenu.addAll(oldSysMenuChildrenList);


        // 处理旧父节点信息
        Long oldParentId = oldSysMenu.getParentId();
        if (oldParentId != null && oldParentId != 0L) {
            SysMenu oldParentSysMenu = super.getById(oldParentId);
            if (oldParentSysMenu == null) {
                throw new AppException("未获取到父级组织信息");
            }

            List<SysMenu> parentSysMenuList = this.findByParentId(oldParentId);
            if (parentSysMenuList != null && parentSysMenuList.size() > 0){
                oldParentSysMenu.setHasChildren(false);
                updateSysMenu.add(oldParentSysMenu);
            }
        }

        super.update(updateSysMenu, UPDATE_FIELD);
    }

    @Override
    public List<SysMenu> findByMenuCode(String menuCode) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("menuCode", menuCode);
        return this.find(params);
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

    public List<SysMenu> findAllChildren(Long parentId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("parentIdLike", parentId);
        return super.find(params);
    }

    /**
     *
     * @param ids 删除
     */
    @Override
    public void deleteMenu(Set<Long> ids) {
        List<SysMenu> sysMenuList = super.findByIds(ids);

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
