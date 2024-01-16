package io.github.toquery.framework.common.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public class EntityFieldConstant {


    public static final String DOMAIN_FIELD_SOFT_DEL = "deleted";

    public static final String ENTITY_TREE_FIELD_ID = "id";

    public static final String ENTITY_TREE_FIELD_PARENTID = "parentId";

    public static final String ENTITY_TREE_FIELD_LEVEL = "level";

    public static final String ENTITY_TREE_FIELD_PARENTIDS = "parentIds";

    public static final String ENTITY_TREE_FIELD_HAS_CHILDREN = "hasChildren";

    public static final String ENTITY_TREE_FIELD_CHILDREN = "children";


    public static final Set<String> ENTITY_TREE_FIELD = Sets.newHashSet(
            ENTITY_TREE_FIELD_ID,
            ENTITY_TREE_FIELD_PARENTID,
            ENTITY_TREE_FIELD_LEVEL,
            ENTITY_TREE_FIELD_PARENTIDS,
            ENTITY_TREE_FIELD_HAS_CHILDREN,
            ENTITY_TREE_FIELD_CHILDREN);
}
