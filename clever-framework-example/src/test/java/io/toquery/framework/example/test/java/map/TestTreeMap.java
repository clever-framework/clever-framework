package io.toquery.framework.example.test.java.map;

import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author toquery
 * @version 1
 */
public class TestTreeMap {

    private Map<Integer,String> map = new TreeMap<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            if (o1 > o2) {
                return -1;
            } else if (o1.equals(o2)) {
                return 0;
            } else {
                return 1;
            }
        }
    });

    @Before
    public void initData(){
        map.put(2,"a");
        map.put(3,"b");
        map.put(1,"a");
    }

    @Test
    public void test1(){

        map.forEach((key,val)->{
            System.out.println(key + " " + val);
        });
    }
}
