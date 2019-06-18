package io.github.toquery.framework.common.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Collection;

/**
 * 实现树状结构
 *
 * @author toquery
 * @version 1
 */
public interface AppEntityTree<T extends AppEntityTree, ID> {

    @JsonIgnore
    public ID getId();
    @JsonIgnore
    public void setId(ID parentId);

    public int getLevel();

    public void setLevel(int level);

    public ID getParentId();

    public void setParentId(ID parentId);

    public String getParentIds();

    public void setParentIds(String parentIds);

    public boolean isHasChildren();

    public boolean getHasChildren();

    public void setHasChildren(boolean hasChildren);

    public T getParent();

    public void setParent(T parent);

    public Collection<T> getChildren();

    public void setChildren(Collection<T> children);
}
