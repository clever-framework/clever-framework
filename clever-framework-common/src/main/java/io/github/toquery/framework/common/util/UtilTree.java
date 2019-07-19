package io.github.toquery.framework.common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.beanutils.PropertyUtils;

import io.github.toquery.framework.common.entity.AppEntityTree;
import lombok.extern.slf4j.Slf4j;

/**
 * 树形层级数据操作类
 *
 * @author toquery
 * @version 1.0.4
 */
@Slf4j
public class UtilTree {

    public static final String TREE_ENTITY_FIELD_ID = "id";

    public static final String TREE_ENTITY_FIELD_PARENTID = "parentId";

    public static final String TREE_ENTITY_FIELD_LEVEL = "level";

    public static final String TREE_ENTITY_FIELD_PARENTIDS = "parentIds";

    public static final String TREE_ENTITY_FIELD_HASCHILDREN = "hasChildren";

    public static final String TREE_ENTITY_FIELD_CHILDREN = "children";

    public static final Set<String> ENTITY_TREE_FIELD = Sets.newHashSet(
            TREE_ENTITY_FIELD_ID,
            TREE_ENTITY_FIELD_PARENTID,
            TREE_ENTITY_FIELD_LEVEL,
            TREE_ENTITY_FIELD_PARENTIDS,
            TREE_ENTITY_FIELD_HASCHILDREN,
            TREE_ENTITY_FIELD_CHILDREN);


    /*
    // 检测是否包含tree数据
    public static <E extends AppEntityTree, ID> List<E> getTreeData(List<E> treeDataList) throws Exception {
        Set<String> fieldSet = Arrays.stream(treeDataList.getClass().getGenericSuperclass().getClass().getDeclaredFields())
                .map(item -> {
                    // 设置 private 为可访问
                    item.setAccessible(true);
                    return item.getName();
                }).collect(Collectors.toSet());

        Sets.SetView<String> stringSetView = Sets.difference(ENTITY_TREE_FIELD, fieldSet);

        if (!stringSetView.isEmpty()){
            throw new Exception("");
        }
        return handleTreeData(treeDataList);
    }
    */

    /**
     * 由简单的数组对象构造树形结构的数据数组
     *
     * @param treeDataList 在结构上不存在层次关系的简单数组数据
     * @return
     */
    public static <E extends AppEntityTree, ID> List<E> getTreeData(List<E> treeDataList) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        //进行数据有效性校验
        if (treeDataList == null || treeDataList.size() < 1) {
            return null;
        }
        //构建treedata的id和实体的映射
        Map<ID, E> treeDataIDMap = Maps.newHashMap();

        for (E appEntityTree : treeDataList) {
            treeDataIDMap.put((ID) PropertyUtils.getProperty(appEntityTree, TREE_ENTITY_FIELD_ID), appEntityTree);
        }

        //格式化之后的结果数组
        List<E> resultList = new ArrayList<>(treeDataList.size());
        for (E node : treeDataList) {
            ID parentId = (ID) PropertyUtils.getProperty(node, TREE_ENTITY_FIELD_PARENTID);
            ID id = (ID) PropertyUtils.getProperty(node, TREE_ENTITY_FIELD_ID);
            //在id映射map中存在父节点的映射
            if (treeDataIDMap.containsKey(parentId) && parentId != id) {
                E parentTreeItemMap = treeDataIDMap.get(parentId);
                List<E> childrenList = (List<E>) PropertyUtils.getProperty(parentTreeItemMap, TREE_ENTITY_FIELD_CHILDREN);
                childrenList.add(node);
                Collections.sort(childrenList);
//                Comparator<AppEntityTree> entityTreeComparator = Comparator.comparing(AppEntityTree::getSortNum);
//                childrenList = new HashSet<>(childrenList);
//                childrenList = childrenList.stream().sorted(entityTreeComparator).collect(Collectors.toSet());
//                PropertyUtils.setProperty(parentTreeItemMap,TREE_ENTITY_FIELD_CHILDREN,childrenList);
//log.info(JSON.toJSONString(childrenList));
            } else {
                //存储根节点数据
                resultList.add(node);
            }
        }
        return resultList;
    }
}
