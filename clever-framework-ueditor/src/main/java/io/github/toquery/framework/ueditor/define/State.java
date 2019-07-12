package io.github.toquery.framework.ueditor.define;

/**
 * @author toquery
 * @version 1
 */
public interface State {
    boolean isSuccess();

    void putInfo(String name, String value);

    void putInfo(String name, long value);

    String toJSONString();
}