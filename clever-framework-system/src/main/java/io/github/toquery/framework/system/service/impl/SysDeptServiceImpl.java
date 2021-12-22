package io.github.toquery.framework.system.service.impl;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysDept;
import io.github.toquery.framework.system.repository.SysDeptRepository;
import io.github.toquery.framework.system.service.ISysDeptService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysDeptServiceImpl extends AppBaseServiceImpl<SysDept, SysDeptRepository> implements ISysDeptService {


    /**
     * 查询条件表达式
     */
    public static final Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("id", "id:EQ");
            put("idIN", "id:IN");
            put("menuName", "menuName:LIKE");
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
    public SysDept saveDept(SysDept sysDept) {

        Long parentId = sysDept.getParentId();

        SysDept parentSysDept = parentId != 0L ? this.getById(parentId) : new SysDept(0L, "根节点", null, null);

        if (parentId != 0L) {
            parentSysDept.setHasChildren(true);
            this.update(parentSysDept, Sets.newHashSet("hasChildren"));
        }

        sysDept.setHasChildren(false);
        sysDept.setLevel(parentSysDept.getLevel() + 1);
        if (Strings.isNullOrEmpty(parentSysDept.getParentIds())) {
            sysDept.setParentIds(parentSysDept.getId().toString());
        } else {
            sysDept.setParentIds(parentSysDept.getParentIds() + "," + parentSysDept.getId());
        }

        return this.save(sysDept);
    }

    @Override
    public SysDept updateDept(SysDept sysDept) {

        SysDept dbSysDept = super.getById(sysDept.getId());

        // 是否需要重建树
        if (!dbSysDept.getParentId().equals(sysDept.getParentId())) {
            this.rebuildSysDeptChildrenTree(sysDept, dbSysDept);
        }

        // 修改当前节点
        Long parentId = sysDept.getParentId();
        if (parentId == 0L) {
            sysDept.setDeptLevel(1);
            sysDept.setParentIds(ROOT_ID);
            sysDept.setTreePath(ROOT_ID + "," + sysDept.getId());
        } else {
            SysDept newParentSysDept = super.getById(parentId);
            sysDept.setDeptLevel(newParentSysDept.getLevel() + 1);
            sysDept.setParentIds(newParentSysDept.getParentIds() + "," + newParentSysDept.getId());
            sysDept.setTreePath(newParentSysDept.getTreePath() + "," + sysDept.getId());
        }

        super.update(sysDept, UPDATE_FIELD);

        return sysDept;
    }

    /**
     * 重建子集树
     */
    public void rebuildSysDeptChildrenTree(SysDept newSysDept, SysDept oldSysDept) throws AppException {

        List<SysDept> updateSysDept = Lists.newArrayList();

        // 获取所有旧机构的子级list
        List<SysDept> oldSysDeptChildrenList = this.findAllChildren(oldSysDept.getId());
        if (oldSysDeptChildrenList == null || oldSysDeptChildrenList.size() <= 0) {
            return;
        }

        // 处理新父节点信息
        String newParentIds;
        Long newParentId = newSysDept.getParentId();

        if (newParentId == 0L) {
            newParentIds = ROOT_ID;
        } else {
            SysDept newParentSysDept = super.getById(newParentId);
            if (newParentSysDept == null) {
                throw new AppException("未获取到父级组织信息");
            }
            newParentIds = newParentSysDept.getParentIds() + "," + newParentSysDept.getId();

            newParentSysDept.setHasChildren(true);
            updateSysDept.add(newParentSysDept);
        }

        // 旧父级ids前缀
        String oldParentIdsPrefix = oldSysDept.getParentIds() + "," + oldSysDept.getId();

        // 新父级ids前缀
        String newParentIdsPrefix = newParentIds + "," + newSysDept.getId();

        oldSysDeptChildrenList = oldSysDeptChildrenList.stream().map(childrenSysDept -> {
            String oldParentIds = childrenSysDept.getParentIds();
            String oldTreePath = childrenSysDept.getTreePath();

            String newSaveParentIds = oldParentIds.replace(oldParentIdsPrefix, newParentIdsPrefix);
            String newTreePath = oldTreePath.replace(oldParentIdsPrefix, newParentIdsPrefix);

            childrenSysDept.setParentIds(newSaveParentIds);
            childrenSysDept.setTreePath(newTreePath);

            childrenSysDept.setDeptLevel(newSaveParentIds.contains(",") ? newSaveParentIds.split(",").length : 1);

            return childrenSysDept;
        }).collect(Collectors.toList());

        updateSysDept.addAll(oldSysDeptChildrenList);


        // 处理旧父节点信息
        Long oldParentId = oldSysDept.getParentId();
        if (oldParentId != null && oldParentId != 0L) {
            SysDept oldParentSysDept = super.getById(oldParentId);
            if (oldParentSysDept == null) {
                throw new AppException("未获取到父级组织信息");
            }

            List<SysDept> parentSysDeptList = this.findByParentId(oldParentId);
            if (parentSysDeptList != null && parentSysDeptList.size() > 0) {
                oldParentSysDept.setHasChildren(false);
                updateSysDept.add(oldParentSysDept);
            }
        }

        super.update(updateSysDept, UPDATE_FIELD);
    }



    @Override
    public List<SysDept> findByParentId(Long parentId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("parentId", parentId);
        return this.list(params);
    }


    @Override
    public List<SysDept> findByParentIds(Set<Long> parentIds) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("parentIdIN", parentIds);
        return this.list(params);
    }

    public List<SysDept> findAllChildren(Long parentId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("parentIdLike", parentId);
        return super.list(params);
    }

    /**
     * @param ids 删除
     */
    @Override
    public void deleteDept(Set<Long> ids) {
        List<SysDept> sysDeptList = super.listByIds(ids);

        List<SysDept> parentSysDeptList = this.findByParentIds(sysDeptList.stream().map(SysDept::getParentId).collect(Collectors.toSet()));

        // 同级兄弟id
        Map<Long, List<SysDept>> brotherIdMap = parentSysDeptList.stream().collect(Collectors.groupingBy(SysDept::getParentId));
        for (SysDept sysDept : sysDeptList) {
            if (!sysDept.getHasChildren()) {
                // 同级菜单信息
                List<SysDept> brotherSysDept = brotherIdMap.get(sysDept.getParentId());
                if (brotherSysDept != null && brotherSysDept.size() == 1) {
                    SysDept parentSysDept = getById(sysDept.getParentId());
                    parentSysDept.setHasChildren(false);
                    this.update(parentSysDept, Sets.newHashSet("hasChildren"));
                }
            }
            this.deleteById(sysDept.getId());
        }
    }
}
