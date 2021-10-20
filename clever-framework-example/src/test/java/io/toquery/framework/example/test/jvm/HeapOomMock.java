package io.toquery.framework.example.test.jvm;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * java.lang.OutOfMemoryError: Java heap space
 * 	at com.toquery.framework.example.test.jvm.HeapOomMock.main(HeapOomMock.java:14)
 * count=3468
 */
public class HeapOomMock {
    public static void main(String[] args) {
        List<byte[]> list = new ArrayList<byte[]>();
        int i = 0;
        boolean flag = true;
        while (flag){
            try {
                i++;
                list.add(new byte[1024 * 1024]);//每次增加一个1M大小的数组对象
            }catch (Throwable e){
                e.printStackTrace();
                flag = false;
                System.out.println("count="+i);//记录运行的次数
            }
        }
    }
}
