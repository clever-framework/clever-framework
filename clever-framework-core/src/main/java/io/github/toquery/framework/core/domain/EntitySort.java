package io.github.toquery.framework.core.domain;


/**
 * 数字越大，排名越靠前
 * @author toquery
 * @version 1
 */
public interface EntitySort {

    public void setSortNum(Integer sortNum);

    public Integer getSortNum();
}
