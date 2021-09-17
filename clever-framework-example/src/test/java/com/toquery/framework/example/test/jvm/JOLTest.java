package com.toquery.framework.example.test.jvm;

import org.openjdk.jol.info.ClassLayout;

/**
 * https://zhuanlan.zhihu.com/p/385732565
 */
public class JOLTest {

    private static Object  o;
    public static void main(String[] args) {
        o = new Object();
        synchronized (o){
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }
    }

}
