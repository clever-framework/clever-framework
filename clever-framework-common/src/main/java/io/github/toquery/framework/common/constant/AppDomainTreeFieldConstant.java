package io.github.toquery.framework.common.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class AppDomainTreeFieldConstant {

    public static final String DOMAIN_TREE_FIELD_ID = "id";

    public static final String DOMAIN_TREE_FIELD_PARENTID = "parentId";

    public static final String DOMAIN_TREE_FIELD_LEVEL = "level";

    public static final String DOMAIN_TREE_FIELD_PARENTIDS = "parentIds";

    public static final String DOMAIN_TREE_FIELD_HAS_CHILDREN = "hasChildren";

    public static final String DOMAIN_TREE_FIELD_CHILDREN = "children";


    public static final Set<String> ENTITY_TREE_FIELD = Sets.newHashSet(
            DOMAIN_TREE_FIELD_ID,
            DOMAIN_TREE_FIELD_PARENTID,
            DOMAIN_TREE_FIELD_LEVEL,
            DOMAIN_TREE_FIELD_PARENTIDS,
            DOMAIN_TREE_FIELD_HAS_CHILDREN,
            DOMAIN_TREE_FIELD_CHILDREN);
}
