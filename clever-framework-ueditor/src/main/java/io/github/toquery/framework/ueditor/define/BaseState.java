package io.github.toquery.framework.ueditor.define;

import io.github.toquery.framework.ueditor.Encoder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
public class BaseState  implements State {
    private boolean state = false;
    private String info = null;
    private Map<String, String> infoMap = new HashMap<>();

    public BaseState() {
        this.state = true;
    }

    public BaseState(boolean state) {
        this.setState(state);
    }

    public BaseState(boolean state, String info) {
        this.setState(state);
        this.info = info;
    }

    public BaseState(boolean state, int infoCode) {
        this.setState(state);
        this.info = AppInfo.getStateInfo(infoCode);
    }

    public boolean isSuccess() {
        return this.state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setInfo(int infoCode) {
        this.info = AppInfo.getStateInfo(infoCode);
    }

    public String toJSONString() {
        return this.toString();
    }

    public String toString() {
        String stateVal = this.isSuccess() ? AppInfo.getStateInfo(0) : this.info;
        StringBuilder builder = new StringBuilder();
        builder.append("{\"state\": \"").append(stateVal).append("\"");

        for (String key : this.infoMap.keySet()) {
            builder.append(",\"").append(key).append("\": \"").append(this.infoMap.get(key)).append("\"");
        }
        builder.append("}");
        return Encoder.toUnicode(builder.toString());
    }

    public void putInfo(String name, String val) {
        this.infoMap.put(name, val);
    }

    public void putInfo(String name, long val) {
        this.putInfo(name, String.valueOf(val));
    }

}
