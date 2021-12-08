package io.github.toquery.framework.system.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.util.ReflectionUtils;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.repository.SysMenuRepository;
import io.github.toquery.framework.system.service.ISysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class SysMenuServiceImpl extends AppBaseServiceImpl<SysMenu, SysMenuRepository> implements ISysMenuService {

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * 查询条件表达式
     */
    public static final Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("id", "id:EQ");
            put("idIN", "id:IN");
            put("menuName", "menuName:LIKE");
            put("menuCode", "menuCode:LIKE");
            put("parentId", "parentId:EQ");
            put("parentIdIN", "parentId:IN");
            put("parentIdsIN", "parentIds:IN");
            put("parentIdsLIKE", "parentIds:LIKE");
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

        if (sysMenuList != null && sysMenuList.size() > 0) {
            throw new AppException("已存在菜单'" + sysMenu.getMenuName() + "'");
        }

        Long parentId = sysMenu.getParentId();

        SysMenu parentSysMenu = parentId != 0L ? this.getById(parentId) : new SysMenu(0L, "根菜单", "root");

        if (parentId != 0L) {
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
    public void scan() {
        // 1. 找到所有使用注解的方法
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(Controller.class);

        Set<String> codesInSources = beansWithAnnotationMap.values().stream()
                .map(beansWithAnnotation -> {
                    log.info(beansWithAnnotation.getClass().getName());
                    return ReflectionUtils.getAllDeclaredMethods(beansWithAnnotation.getClass());
                })
                .flatMap(methods -> Stream.of(methods)
                        .filter(method -> {
                            PreAuthorize preAuthorize = AnnotationUtils.findAnnotation(method, PreAuthorize.class);
                            return preAuthorize != null && !Strings.isNullOrEmpty(preAuthorize.value());
                        })
                        .map(method -> AnnotationUtils.findAnnotation(method, PreAuthorize.class).value()
                                .replace("hasAnyAuthority", "")
                                .replace("'", "")
                                .replace(",", "")
                                .replace("(", "")
                                .replace(")", "")
                        )).collect(Collectors.toSet());

        // 2. 获取数据库中的所有菜单
        List<SysMenu> dbSysMenus = super.find();

        // 数据库中的code
        Map<String, SysMenu> dbSysMenuMap = dbSysMenus.stream().collect(Collectors.toMap(SysMenu::getMenuCode, sysMenu -> sysMenu, (sysMenu1, sysMenu2) -> sysMenu1));
        Set<String> dbMenuCodes = dbSysMenus.stream().map(SysMenu::getMenuCode).collect(Collectors.toSet());

        Set<String> newCodes = codesInSources.stream()
                .flatMap(code -> {
                    if (code.contains(":")) {
                        Set<String> codes = Sets.newHashSet();
                        String[] splitCodes = code.split(":");
                        for (int i = 0; i < splitCodes.length; i++) {
                            codes.add(String.join(":", Arrays.asList(splitCodes).subList(0, i + 1)));
                        }
                        return codes.stream();
                    } else {
                        return Stream.of(code);
                    }
                })
                .filter(code -> !dbMenuCodes.contains(code))
                .collect(Collectors.toSet());

        Map<String, SysMenu> newSysMenuMap = new TreeMap<>();

        newCodes.forEach(code -> {
            SysMenu sysMenu = new SysMenu(code, code);
            sysMenu.preInsert();
            sysMenu.setHasChildren(false);
            newSysMenuMap.put(code, sysMenu);
        });

        List<SysMenu> updateSysMenus = Lists.newArrayList();

        for (SysMenu sysMenu : newSysMenuMap.values()) {
            String menuCode = sysMenu.getMenuCode();
            if (menuCode.contains(":")) {
                String parentCode = menuCode.substring(0, menuCode.lastIndexOf(":"));
                SysMenu dbParentSysMenu = dbSysMenuMap.get(parentCode);
                SysMenu newParentSysMenu = newSysMenuMap.get(parentCode);
                if (dbParentSysMenu != null) {
                    dbParentSysMenu.setHasChildren(true);
                    sysMenu.setMenuLevel(dbParentSysMenu.getMenuLevel() + 1);
                    sysMenu.setParentId(dbParentSysMenu.getId());
                    sysMenu.setParentIds(dbParentSysMenu.getParentIds() + "," + dbParentSysMenu.getId());
                    sysMenu.setTreePath(dbParentSysMenu.getTreePath() + "," + sysMenu.getId());
                    updateSysMenus.add(dbParentSysMenu);
                } else if (newParentSysMenu != null) {
                    newParentSysMenu.setHasChildren(true);
                    sysMenu.setMenuLevel(newParentSysMenu.getMenuLevel() + 1);
                    sysMenu.setParentId(newParentSysMenu.getId());
                    sysMenu.setParentIds(newParentSysMenu.getParentIds() + "," + newParentSysMenu.getId());
                    sysMenu.setTreePath(newParentSysMenu.getTreePath() + "," + sysMenu.getId());
                } else {
                    throw new AppException("未找到" + parentCode + "父菜单");
                }
            } else {
                sysMenu.setMenuLevel(1);
                sysMenu.setParentId(0L);
                sysMenu.setParentIds("0");
                sysMenu.setTreePath("0," + sysMenu.getId());
            }
        }

        if (updateSysMenus.size() > 0) {
            super.update(updateSysMenus, UPDATE_FIELD);
        }

        if (newSysMenuMap.values().size() > 0){
            log.info("新增菜单：{}", newSysMenuMap.values().size());
            super.save(Lists.newArrayList(newSysMenuMap.values()));
        }
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
            if (parentSysMenuList != null && parentSysMenuList.size() > 0) {
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
        params.put("parentIdsLIKE", parentId);
        return super.find(params);
    }

    /**
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
                if (brotherSysMenu != null && brotherSysMenu.size() == 1) {
                    SysMenu parentSysMenu = getById(sysMenu.getParentId());
                    parentSysMenu.setHasChildren(false);
                    this.update(parentSysMenu, Sets.newHashSet("hasChildren"));
                }
            }
            this.deleteById(sysMenu.getId());
        }
    }
}
