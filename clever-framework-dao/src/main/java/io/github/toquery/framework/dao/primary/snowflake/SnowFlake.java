package io.github.toquery.framework.dao.primary.snowflake;

import com.google.common.base.Strings;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * twitter的snowflake算法 -- java实现
 *
 * @author toquery
 */
public class SnowFlake {

    /**
     * 起始的时间戳
     */
    private final static long START_TIMESTAMPS = 0L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;   //机器标识占用的位数
    private final static long DATACENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATACENTER_NUM = ~(-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = ~(-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = ~(-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long dataCenterId;  //数据中心
    private long machineId;     //机器标识
    private long sequence = 0L; //序列号
    private long lastTimestamps = -1L;//上一次时间戳


    public SnowFlake() {
        dataCenterId = defaultDataCenterId();
        machineId = defaultWorkerId(dataCenterId);
    }

    public SnowFlake(long dataCenterId, long machineId) {
        if (dataCenterId > MAX_DATACENTER_NUM || dataCenterId < 0) {
            throw new IllegalArgumentException("dataCenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 数据标识id部分
     *
     * @return 数据标识id
     */
    protected static long defaultDataCenterId() {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                if (null != mac) {
                    id = ((0x000000FF & (long) mac[mac.length - 1]) | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                    id = id % (MAX_DATACENTER_NUM + 1);
                }
            }
        } catch (Exception e) {
            System.err.println(" get max data center id : " + e.getMessage());
        }
        return id;
    }


    /**
     * 获取默认机器ID
     *
     * @param dataCenterId 数据中心id
     * @return workerId
     */
    protected static long defaultWorkerId(long dataCenterId) {
        StringBuilder pidStringBuilder = new StringBuilder();
        pidStringBuilder.append(dataCenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!Strings.isNullOrEmpty(name)) {
            // GET jvmPid
            pidStringBuilder.append(name.split("@")[0]);
        }
        //MAC + PID 的 hashcode 获取16个低位
        return (pidStringBuilder.toString().hashCode() & 0xffff) % (MAX_MACHINE_NUM + 1);
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currTimestamps = getNewTimestamps();
        if (currTimestamps < lastTimestamps) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currTimestamps == lastTimestamps) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currTimestamps = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTimestamps = currTimestamps;

        return (currTimestamps - START_TIMESTAMPS) << TIMESTMP_LEFT //时间戳部分
                | dataCenterId << DATACENTER_LEFT       //数据中心部分
                | machineId << MACHINE_LEFT             //机器标识部分
                | sequence;                             //序列号部分
    }

    private long getNextMill() {
        long mill = getNewTimestamps();
        while (mill <= lastTimestamps) {
            mill = getNewTimestamps();
        }
        return mill;
    }

    private long getNewTimestamps() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
//        for (int j = 0; j < 100000; j++) {
//            Runnable runnable = () -> {
        SnowFlake snowFlake = new SnowFlake();

        for (int i = 0; i < (1 << 12); i++) {
            System.out.println(Thread.currentThread().getName() + " : " + snowFlake.nextId());
        }
//            };
//            Thread t = new Thread(runnable);
//            t.start();
        //}

    }
}
