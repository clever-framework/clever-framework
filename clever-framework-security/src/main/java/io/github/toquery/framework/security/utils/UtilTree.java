package io.github.toquery.framework.security.utils;

import io.github.toquery.framework.common.entity.AppEntityTree;
import io.github.toquery.framework.security.system.domain.SysMenu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树形层级数据操作类
 *
 * @author toquery
 * @version 1.0.4
 */
public class UtilTree {

    /**
     * 由简单的数组对象构造树形结构的数据数组
     *
     * @param treeDataList 在结构上不存在层次关系的简单数组数据
     * @return
     */
    public static  List<SysMenu> getTreeData(List<SysMenu> treeDataList) {
        //进行数据有效性校验
        if (treeDataList == null || treeDataList.size() < 1) {
            return null;
        }
        //构建treedata的id和实体的映射
        Map<Long, SysMenu> treeDataIDMap = treeDataList.stream().collect(Collectors.toMap(SysMenu::getId, item -> item));
        //格式化之后的结果数组
        List<SysMenu> resultList = new ArrayList<>(treeDataList.size());
        for (SysMenu node : treeDataList) {
            Long parentId = node.getParentId();
            Long id = node.getId();
            //在id映射map中存在父节点的映射
            if (treeDataIDMap.containsKey(parentId) && !parentId.equals(id)) {
                SysMenu parentTreeItemMap = treeDataIDMap.get(parentId);
                Collection<SysMenu> childrenList = parentTreeItemMap.getChildren();
                childrenList.add(node);
            } else {
                //存储根节点数据
                resultList.add(node);
            }
        }
        return resultList;
    }
}
