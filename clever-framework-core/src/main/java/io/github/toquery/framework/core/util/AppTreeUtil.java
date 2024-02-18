package io.github.toquery.framework.core.util;

import cn.hutool.core.util.ReflectUtil;
import io.github.toquery.framework.common.constant.EntityFieldConstant;
import io.github.toquery.framework.core.domain.EntityTree;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 树形层级数据操作类
 *
 * @author toquery
 * @version 1.0.4
 */
@Slf4j
public class AppTreeUtil {

    /**
     * 由简单的数组对象构造树形结构的数据数组
     *
     * @param treeDataList 在结构上不存在层次关系的简单数组数据
     * @param <E>          实现AppEntityTree 的实体
     * @return 树状结构
     * @throws Exception 反射失败异常
     */
    public static <E extends EntityTree<?>> List<E> getTreeData(List<E> treeDataList) throws Exception {
        //进行数据有效性校验
        if (treeDataList == null || treeDataList.isEmpty()) {
            return null;
        }
        //构建treedata的id和实体的映射
        Map<Long, E> treeDataIDMap = new HashMap<>();

        for (E appEntityTree : treeDataList) {
            Long id = (Long) ReflectUtil.getFieldValue(appEntityTree, EntityFieldConstant.ENTITY_TREE_FIELD_ID);
            treeDataIDMap.put(id, appEntityTree);
        }

        //格式化之后的结果数组
        List<E> resultList = new ArrayList<>(treeDataList.size());
        for (E node : treeDataList) {
            Long parentId = (Long) ReflectUtil.getFieldValue(node, EntityFieldConstant.ENTITY_TREE_FIELD_PARENTID);
            Long id = (Long) ReflectUtil.getFieldValue(node, EntityFieldConstant.ENTITY_TREE_FIELD_ID);
            //在id映射map中存在父节点的映射
            if (treeDataIDMap.containsKey(parentId) && parentId != id) {
                E parentTreeItemMap = treeDataIDMap.get(parentId);
                List<E> childrenList = (List<E>) ReflectUtil.getFieldValue(parentTreeItemMap, EntityFieldConstant.ENTITY_TREE_FIELD_CHILDREN);
                if (childrenList == null) {
                    childrenList = new ArrayList<>();
                }
                childrenList.add(node);
                // Collections.sort(childrenList);
                ReflectUtil.setFieldValue(parentTreeItemMap, EntityFieldConstant.ENTITY_TREE_FIELD_CHILDREN, childrenList);
                // 重新判定是否有子集
                if (!childrenList.isEmpty()) {
                    ReflectUtil.setFieldValue(parentTreeItemMap, EntityFieldConstant.ENTITY_TREE_FIELD_HAS_CHILDREN, false);
                }
            } else {
                //存储根节点数据
                resultList.add(node);
            }
        }
        return resultList;
    }
}
