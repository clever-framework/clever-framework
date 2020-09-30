package com.toquery.framework.example.test.jvm;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;


/**
 * -XX:MetaspaceSize=8m -XX:MaxMetaspaceSize=50m
 */
public class JavaHeapSpaceOomMock {

    static String base = "string";


    /**
     * java.lang.OutOfMemoryError: Java heap space
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        ClassLoadingMXBean loadingBean = ManagementFactory.getClassLoadingMXBean();

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            String str = base + base;
            base = str;
            list.add(str.intern());

            //显示数量信息（共加载过的类型数目，当前还有效的类型数目，已经被卸载的类型数目）
            System.out.println("total: " + loadingBean.getTotalLoadedClassCount());
            System.out.println("active: " + loadingBean.getLoadedClassCount());
            System.out.println("unloaded: " + loadingBean.getUnloadedClassCount());
            System.out.println("--------------------------------------------");
        }
    }
}
