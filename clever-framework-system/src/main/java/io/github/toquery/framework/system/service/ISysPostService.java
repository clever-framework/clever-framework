package io.github.toquery.framework.system.service;

import com.google.common.collect.Sets;
import io.github.toquery.framework.crud.service.AppBaseService;
import io.github.toquery.framework.system.entity.SysPost;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysPostService extends AppBaseService<SysPost> {

    public static final String ROOT_ID = "0";

    public static final Set<String> UPDATE_FIELD = Sets.newHashSet("deptName", "deptCode", "sortNum", "parentId", "parentIds", "treePath", "hasChildren");

}
