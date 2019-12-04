package com.toquery.framework.demo.test.bloom;

import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

public class BloomFilterDemo {

    /**
     * 当以上代码运行后，控制台会输出以下结果(误判率 fpp为 0.03)：
     * 已匹配数量 1000309
     * <p>
     * 很明显以上的输出结果已经出现了误报，因为相比预期的结果多了 309 个元素，误判率为：
     * 309/(1000000 + 10000) * 100 ≈ 0.030594059405940593
     * <p>
     * 修改为 0.0002 输出结果为：
     * 已匹配数量 1000003
     * <p>
     * https://juejin.im/post/5de1e37c5188256e8e43adfc
     */

    @Test
    public void testBloomFilter() {
        int total = 1000000; // 总数量

        // 误判率 fpp 的默认值是 0.03：
        // BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total);

        // 误判率 fpp 为 0.0002：
        BloomFilter<CharSequence> bf = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), total, 0.0002);
        // 初始化 1000000 条数据到过滤器中
        for (int i = 0; i < total; i++) {
            bf.put("" + i);
        }
        // 判断值是否存在过滤器中
        int count = 0;
        for (int i = 0; i < total + 10000; i++) {
            if (bf.mightContain("" + i)) {
                count++;
            }
        }
        System.out.println("已匹配数量 " + count);
    }

}
