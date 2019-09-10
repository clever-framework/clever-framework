package io.github.toquery.framework.core.util;

import com.google.common.collect.Maps;
import io.github.toquery.framework.common.constant.AppDomainTreeFieldConstant;
import io.github.toquery.framework.core.domain.AppEntityTree;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
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
    public static <E extends AppEntityTree, ID> List<E> getTreeData(List<E> treeDataList) throws Exception{
        //进行数据有效性校验
        if (treeDataList == null || treeDataList.size() < 1) {
            return null;
        }
        //构建treedata的id和实体的映射
        Map<ID, E> treeDataIDMap = Maps.newHashMap();

        for (E appEntityTree : treeDataList) {
            treeDataIDMap.put((ID) PropertyUtils.getProperty(appEntityTree, AppDomainTreeFieldConstant.DOMAIN_TREE_FIELD_ID), appEntityTree);
        }

        //格式化之后的结果数组
        List<E> resultList = new ArrayList<>(treeDataList.size());
        for (E node : treeDataList) {
            ID parentId = (ID) PropertyUtils.getProperty(node, AppDomainTreeFieldConstant.DOMAIN_TREE_FIELD_PARENTID);
            ID id = (ID) PropertyUtils.getProperty(node, AppDomainTreeFieldConstant.DOMAIN_TREE_FIELD_ID);
            //在id映射map中存在父节点的映射
            if (treeDataIDMap.containsKey(parentId) && parentId != id) {
                E parentTreeItemMap = treeDataIDMap.get(parentId);
                List<E> childrenList = (List<E>) PropertyUtils.getProperty(parentTreeItemMap, AppDomainTreeFieldConstant.DOMAIN_TREE_FIELD_CHILDREN);
                if (childrenList == null) {
                    childrenList = new ArrayList<>();
                }
                childrenList.add(node);
                Collections.sort(childrenList);
                PropertyUtils.setProperty(parentTreeItemMap, AppDomainTreeFieldConstant.DOMAIN_TREE_FIELD_CHILDREN, childrenList);
            } else {
                //存储根节点数据
                resultList.add(node);
            }
        }
        return resultList;
    }
}
