package io.github.toquery.framework.datasource.config;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DataSourceContextHolder {

    private static final ThreadLocal<String> holder = new ThreadLocal<>();

    public static final String DEFAULT_DATA_SOURCE = "master";

    public static void setDataSource(String type) {
        holder.set(type);
    }

    public static String getDataSource() {
        String lookUpKey = holder.get();
        return lookUpKey == null ? DEFAULT_DATA_SOURCE : lookUpKey;
    }

    public static void clear() {
        holder.remove();
    }
}
