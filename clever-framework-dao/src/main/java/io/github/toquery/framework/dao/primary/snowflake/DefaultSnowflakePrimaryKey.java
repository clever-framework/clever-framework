package io.github.toquery.framework.dao.primary.snowflake;

import com.google.common.base.Splitter;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import com.google.common.util.concurrent.RateLimiter;
import io.github.toquery.framework.dao.util.UtilNumber;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
public class DefaultSnowflakePrimaryKey implements SnowflakePrimaryKey {

    //当前机器的ip地址
    protected static final Integer[] IP;

    static {
        String hostIp = "";
        try {
            hostIp = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Integer> tmpIp = null;
        //分割ip地址
        List<String> ipParts = Splitter.on('.').trimResults().splitToList(hostIp);
        if (ipParts != null && ipParts.size() == 4) {
            tmpIp = Lists.newArrayListWithCapacity(4);
            for (String ipPart : ipParts) {
                tmpIp.add(Ints.tryParse(ipPart));
            }
        }
        if (tmpIp == null) {
            IP = new Integer[]{0, 0, 0, 0};
        } else {
            IP = tmpIp.toArray(new Integer[]{});
        }
    }

    @Setter
    @Getter
    private Map<EnumPrimaryKeyRule, Integer> idItems;

    private RateLimiter idRateLimiter;

    @Getter
    private LoadingCache<Long, Set<Integer>> numCache = CacheBuilder.newBuilder()
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .softValues()
            .build(new CacheLoader<Long, Set<Integer>>() {
                @Override
                public Set<Integer> load(Long key) throws Exception {
                    return Sets.newHashSet();
                }
            });

    private static Random sequenceRandom = new Random();

    @Getter
    private int maxIdsPerSecond = 0;

    /**
     * 设置当前的id组成信息
     *
     * @param idItems
     */
    public void setIdItems(Map<EnumPrimaryKeyRule, Integer> idItems) {
        this.idItems = idItems;
        //每毫秒的个数
        int secondNumLength = this.idItems.get(EnumPrimaryKeyRule.num);

        //通过令牌桶的控流算法，控制每秒id的生成个数。并且为每秒生成的id个数保留一定的20%的余量
        this.maxIdsPerSecond = Double.valueOf(UtilNumber.getMaxNum(secondNumLength) * 0.8).intValue();

        this.idRateLimiter = RateLimiter.create(maxIdsPerSecond);

        log.info("每秒生成{}个左右的id", maxIdsPerSecond);
    }

    @Override
    public String getName() {
        return "默认的snowflake id生成器";
    }

    /**
     * 按照秒生成唯一的随机数。
     */
    @Override
    public long getNumIndex(int binaryLength, long timeSeconds, Object object) {
        //限制id生成的速度
        idRateLimiter.acquire();

        //生成一个随机的id
        Set<Integer> numSets = numCache.getUnchecked(timeSeconds);
        int maxNum = UtilNumber.getMaxNum(binaryLength);
        int random = 0;

        synchronized (IP) {
            do {
                random = sequenceRandom.nextInt(maxNum);
            } while (numSets.contains(random));
            //加入组合
            numSets.add(random);
        }

        return random;
    }

    /**
     * 获取自定义字段内容索引
     */
    @Override
    public long getCustomIndex(int binaryLength, long timeSeconds, Object object) {
        return 0;
    }

    /**
     * 获取机器的标识，默认收集第三部分，<b>在docker中宿主机的ip默认是第三部分</b>
     */
    @Override
    public long getMachineIndex(int binaryLength, long timeSeconds, Object object) {
        return IP[IP.length - 1];
    }

    /**
     * 获取机房索引
     */
    @Override
    public long getMachineRoomIndex(int binaryLength, long timeSeconds, Object object) {
        return 0;
    }

    /**
     * 获取业务线索引
     */
    @Override
    public long getBusinessIndex(int binaryLength, long timeSeconds, Object object) {
        return 0;
    }

    /**
     * 获取时间索引
     */
    @Override
    public long getTimeIndex(Integer binaryLength, long timeSeconds, Object object) {
        return timeSeconds;
    }

}
