package com.toquery.framework.demo.test.usercf;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 基于用户的协同过滤推荐算法实现
 * A a b d
 * B a c
 * C b e
 * D c d e
 *
 * @author toquery
 * @version 1
 */
public class TestUserCF {

    private Map<String, Set<String>> data = Maps.newHashMap();



    @Before
    public void initData() {
//        Set<String> userSet = Sets.newHashSet("A", "B", "C", "D");

        data.put("A", Sets.newHashSet("a", "b", "d"));
        data.put("B", Sets.newHashSet("a", "c"));
        data.put("C", Sets.newHashSet("b", "e"));
        data.put("D", Sets.newHashSet("c", "d", "e"));




    }

    @Test
    public void test1() {
        int dimension = data.size();

        //建立用户稀疏矩阵，用于用户相似度计算【相似度矩阵】
        int[][] sparseMatrix = new int[dimension][dimension];

        //存储每一个用户对应的不同物品总数  eg: A 3
        Map<String, Integer> userItemLength = new HashMap<>();
        //建立物品到用户的倒排表 eg: a A B
        Map<String, Set<String>> itemUserCollection = new HashMap<>();
        //辅助存储物品集合
        Set<String> items = new HashSet<>();
        //辅助存储每一个用户的用户ID映射
        Map<String, Integer> userID = new HashMap<>();

        //辅助存储每一个ID对应的用户映射
        Map<Integer, String> idUser = new HashMap<>();



    }
}
