package io.github.toquery.framework.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.core.util.AppReflectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.mapper.SysMenuMapper;
import io.github.toquery.framework.system.service.ISysMenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.Arrays;
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
@RequiredArgsConstructor
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements ISysMenuService {

    private final ApplicationContext applicationContext;


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
            LambdaUpdateWrapper<SysMenu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            lambdaUpdateWrapper.eq(SysMenu::getId, parentSysMenu.getId());
            lambdaUpdateWrapper.set(SysMenu::getHasChildren, parentSysMenu.getHasChildren());
            this.update(parentSysMenu, lambdaUpdateWrapper);
        }

        sysMenu.setHasChildren(false);
        sysMenu.setLevel(parentSysMenu.getLevel() + 1);
        if (Strings.isNullOrEmpty(parentSysMenu.getParentIds())) {
            sysMenu.setParentIds(parentSysMenu.getId().toString());
        } else {
            sysMenu.setParentIds(parentSysMenu.getParentIds() + "," + parentSysMenu.getId());
        }

        this.save(sysMenu);
        return sysMenu;
    }

    @Override
    public void scanAndInsertMenus(boolean generateView) {
        // 1. 找到所有使用注解的方法
        Map<String, Object> beansWithAnnotationMap = applicationContext.getBeansWithAnnotation(Controller.class);

        Set<String> codesInSources = beansWithAnnotationMap.values().stream()
                .map(beansWithAnnotation -> {
                    log.info(beansWithAnnotation.getClass().getName());
                    return AppReflectionUtils.getAllDeclaredMethods(beansWithAnnotation.getClass());
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
        List<SysMenu> dbSysMenus = super.list();

        // 数据库中的code
        Map<String, SysMenu> dbSysMenuMap = dbSysMenus.stream().collect(Collectors.toMap(SysMenu::getMenuCode, sysMenu -> sysMenu, (sysMenu1, sysMenu2) -> sysMenu1));
        Set<String> dbMenuCodes = dbSysMenus.stream().map(SysMenu::getMenuCode).collect(Collectors.toSet());

        Set<String> newCodes = codesInSources.stream()
                .flatMap(code -> {
                    if (code.contains(":")) {
                        Set<String> codes = Sets.newHashSet();
                        String[] splitCodes = code.split(":");
                        for (int i = 0; i < splitCodes.length; i++) {
                            List<String> menuCodes = Arrays.asList(splitCodes).subList(0, i + 1);
                            String menuCodesStr = String.join(":", menuCodes);
                            // view 视图 , 只有在 xxx:xxx 才会增加视图菜单
                            if (generateView && menuCodes.size() == 2) {
                                codes.add(menuCodesStr + ":view");
                            }
                            codes.add(menuCodesStr);
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
            super.updateBatchById(updateSysMenus);
        }

        if (newSysMenuMap.values().size() > 0){
            log.info("新增菜单：{}", newSysMenuMap.values().size());
            super.saveBatch(newSysMenuMap.values());
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

        LambdaUpdateWrapper<SysMenu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(SysMenu::getId, sysMenu.getId());
        lambdaUpdateWrapper.set(SysMenu::getMenuName, sysMenu.getMenuName());
        lambdaUpdateWrapper.set(SysMenu::getMenuCode, sysMenu.getMenuCode());
        lambdaUpdateWrapper.set(SysMenu::getSortNum, sysMenu.getSortNum());
        lambdaUpdateWrapper.set(SysMenu::getParentId, sysMenu.getParentId());
        lambdaUpdateWrapper.set(SysMenu::getParentIds, sysMenu.getParentIds());
        lambdaUpdateWrapper.set(SysMenu::getTreePath, sysMenu.getTreePath());
        lambdaUpdateWrapper.set(SysMenu::getHasChildren, sysMenu.getHasChildren());
        super.update(sysMenu, lambdaUpdateWrapper);

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

        super.updateBatchById(updateSysMenu);
    }

    @Override
    public List<SysMenu> findByMenuCode(String menuCode) {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(SysMenu::getMenuCode, menuCode);
        return super.list(lambdaQueryWrapper);
    }


    @Override
    public List<SysMenu> findByParentId(Long parentId) {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SysMenu::getParentId, parentId);
        return super.list(lambdaQueryWrapper);
    }


    @Override
    public List<SysMenu> findByParentIds(Set<Long> parentIds) {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(SysMenu::getParentId, parentIds);
        return super.list(lambdaQueryWrapper);
    }

    public List<SysMenu> findAllChildren(Long parentId) {
        LambdaQueryWrapper<SysMenu> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(SysMenu::getParentIds, parentId);
        return super.list(lambdaQueryWrapper);
    }

    /**
     * @param ids 删除
     */
    @Override
    public void deleteMenu(Set<Long> ids) {
        List<SysMenu> sysMenuList = super.listByIds(ids);
        List<SysMenu> parentSysMenuList = this.findByParentIds(sysMenuList.stream().map(SysMenu::getParentId).collect(Collectors.toSet()));
        // 同级兄弟id
        Map<Long, List<SysMenu>> brotherIdMap = parentSysMenuList.stream().collect(Collectors.groupingBy(SysMenu::getParentId));
        for (SysMenu sysMenu : sysMenuList) {
            if (!sysMenu.getHasChildren()) {
                // 同级菜单信息
                List<SysMenu> brotherSysMenu = brotherIdMap.get(sysMenu.getParentId());
                if (brotherSysMenu != null && brotherSysMenu.size() == 1) {
                    SysMenu parentSysMenu = super.getById(sysMenu.getParentId());
                    parentSysMenu.setHasChildren(false);
                    LambdaUpdateWrapper<SysMenu> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
                    lambdaUpdateWrapper.eq(SysMenu::getId, parentSysMenu.getId());
                    lambdaUpdateWrapper.set(SysMenu::getHasChildren, parentSysMenu.getHasChildren());
                    this.update(parentSysMenu, lambdaUpdateWrapper);
                }
            }
            this.removeById(sysMenu.getId());
        }
    }
}
