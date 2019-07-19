package io.github.toquery.framework.common.entity;

import java.util.List;

/**
 * 实现树状结构
 *
 * @author toquery
 * @version 1
 */
public interface AppEntityTree<E> extends AppBaseEntitySort,Comparable<E> {

    public int getLevel();

    public void setLevel(int level);

    public String getParentIds();

    public void setParentIds(String parentIds);

    public boolean isHasChildren();

    public boolean getHasChildren();

    public void setHasChildren(boolean hasChildren);

    public E getParent();

    public void setParent(E parent);

    public List<E> getChildren();

    public void setChildren(List<E> children);
}
