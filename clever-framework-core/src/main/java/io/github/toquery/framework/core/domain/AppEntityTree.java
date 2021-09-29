package io.github.toquery.framework.core.domain;

import java.util.List;

/**
 * 实现树状结构
 *
 * @author toquery
 * @version 1
 */
public interface AppEntityTree<E> extends AppEntitySort, Comparable<E> {

    Long getId();

    void setId(Long id);

    Long getParentId();

    void setParentId(Long parentId);

    int getLevel();

    void setLevel(int level);

    String getParentIds();

    void setParentIds(String parentIds);

    boolean isHasChildren();

    boolean getHasChildren();

    void setHasChildren(boolean hasChildren);

    E getParent();

    void setParent(E parent);

    List<E> getChildren();

    void setChildren(List<E> children);
}
