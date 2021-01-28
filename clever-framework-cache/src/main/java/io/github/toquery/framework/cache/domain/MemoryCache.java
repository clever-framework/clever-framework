package io.github.toquery.framework.cache.domain;

import lombok.Data;

@Data
public class MemoryCache {
    //json字符串
    private String value;

    //缓存时间
    private long exp;
    
    //存入时间
    private long saveTime;
}

