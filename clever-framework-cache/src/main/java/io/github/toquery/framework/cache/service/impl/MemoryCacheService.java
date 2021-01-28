package io.github.toquery.framework.cache.service.impl;

import io.github.toquery.framework.cache.domain.MemoryCache;
import io.github.toquery.framework.cache.service.ICacheService;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 *
 */
public class MemoryCacheService implements ICacheService {

    private Map<String, MemoryCache> softHashMap = new ConcurrentReferenceHashMap<>();

    public void set(String key, String value, long exp) {
        MemoryCache LocalCache = new MemoryCache();
        LocalCache.setValue(value);
        LocalCache.setSaveTime(System.currentTimeMillis());
        LocalCache.setExp(exp);
        softHashMap.put(key, LocalCache);
    }


    @Override
    public boolean set(String key, Object value, long time) {
        return false;
    }

    @Override
    public long incr(String key, long delta) {
        return 0;
    }

    @Override
    public long decr(String key, long delta) {
        return 0;
    }

    @Override
    public Object hget(String key, String item) {
        return null;
    }

    @Override
    public Map<Object, Object> hmget(String key) {
        return null;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map) {
        return false;
    }

    @Override
    public boolean hmset(String key, Map<String, Object> map, long time) {
        return false;
    }

    @Override
    public boolean hset(String key, String item, Object value) {
        return false;
    }

    @Override
    public boolean hset(String key, String item, Object value, long time) {
        return false;
    }

    @Override
    public void hdel(String key, Object... item) {

    }

    @Override
    public boolean hHasKey(String key, String item) {
        return false;
    }

    @Override
    public double hincr(String key, String item, double delta) {
        return 0;
    }

    @Override
    public double hdecr(String key, String item, double by) {
        return 0;
    }

    @Override
    public Set<Object> sGet(String key) {
        return null;
    }

    @Override
    public boolean sHasKey(String key, Object value) {
        return false;
    }

    @Override
    public long sSet(String key, Object... values) {
        return 0;
    }

    @Override
    public long sSetAndTime(String key, long time, Object... values) {
        return 0;
    }

    @Override
    public long sGetSetSize(String key) {
        return 0;
    }

    @Override
    public long setRemove(String key, Object... values) {
        return 0;
    }

    @Override
    public List<Object> lGet(String key, long start, long end) {
        return null;
    }

    @Override
    public long lGetListSize(String key) {
        return 0;
    }

    @Override
    public Object lGetIndex(String key, long index) {
        return null;
    }

    @Override
    public boolean lSet(String key, Object value) {
        return false;
    }

    @Override
    public boolean lSet(String key, Object value, long time) {
        return false;
    }

    @Override
    public boolean lSet(String key, List<Object> value) {
        return false;
    }

    @Override
    public boolean lSet(String key, List<Object> value, long time) {
        return false;
    }

    @Override
    public boolean lUpdateIndex(String key, long index, Object value) {
        return false;
    }

    @Override
    public long lRemove(String key, long count, Object value) {
        return 0;
    }

    @Override
    public String deleteAndGet(String key) {
        String value = get(key);
        if (value != null) {
            softHashMap.remove(key);
        }
        return value;
    }

    public void delete(String key) {
        softHashMap.remove(key);
    }

    @Override
    public String get(String key) {
        MemoryCache LocalCache = softHashMap.get(key);
        //说明没过期
        if (LocalCache != null && (LocalCache.getExp() <= 0 || ((System.currentTimeMillis() - LocalCache.getSaveTime()) <= LocalCache.getExp() * 1000))) {
            return LocalCache.getValue();
        }
        softHashMap.remove(key);
        return null;
    }

    @Override
    public boolean set(String key, Object value) {
        return false;
    }

    public void clear(String cachePrefix) {
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                softHashMap.remove(k);
            }
        });
    }

    public int size(String cachePrefix) {
        AtomicInteger size = new AtomicInteger(0);
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                size.getAndIncrement();
            }
        });
        return size.get();
    }

    public boolean existKey(String key) {
        return softHashMap.containsKey(key);
    }

    public Set<String> keys(String cachePrefix) {
        return softHashMap.keySet().stream().filter(k -> k.startsWith(cachePrefix)).collect(Collectors.toSet());
    }

    public Collection<String> values(String cachePrefix) {
        List<String> values = new ArrayList<>();
        softHashMap.forEach((k, v) -> {
            if (k.startsWith(cachePrefix)) {
                String value = get(k);
                if (value != null) {
                    values.add(value);
                }
            }
        });
        return values;
    }

    @Override
    public boolean expire(String key, long time) {
        return false;
    }

    @Override
    public long getExpire(String key) {
        return 0;
    }

    @Override
    public boolean hasKey(String key) {
        return false;
    }

    @Override
    public void del(String... key) {

    }
}
