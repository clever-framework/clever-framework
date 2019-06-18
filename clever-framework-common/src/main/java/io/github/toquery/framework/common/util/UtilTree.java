package io.github.toquery.framework.common.util;

import io.github.toquery.framework.common.entity.AppEntityTree;

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
    public static <E extends AppEntityTree<E, ID>, ID> List<E> getTreeData(List<E> treeDataList) {
        //进行数据有效性校验
        if (treeDataList == null || treeDataList.size() < 1) {
            return null;
        }
        //构建treedata的id和实体的映射
        Map<ID, E> treeDataIDMap = treeDataList.stream().collect(Collectors.toMap(AppEntityTree::getId, item -> item));
        //格式化之后的结果数组
        List<E> resultList = new ArrayList<>(treeDataList.size());
        for (E node : treeDataList) {
            ID parentId = node.getParentId();
            ID id = node.getId();
            //在id映射map中存在父节点的映射
            if (treeDataIDMap.containsKey(parentId) && parentId != id) {
                E parentTreeItemMap = treeDataIDMap.get(parentId);
                Collection<E> childrenList = parentTreeItemMap.getChildren();
                childrenList.add(node);
            } else {
                //存储根节点数据
                resultList.add(node);
            }
        }
        return resultList;
    }
}
