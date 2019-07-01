package io.github.toquery.framework.ueditor;


import io.github.toquery.framework.ueditor.define.ActionMap;
import io.github.toquery.framework.ueditor.properties.AppUeditorProperties;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
public class UeditorConfigManager {

    private AppUeditorProperties appUeditorProperties;

    public UeditorConfigManager() {
    }

    public UeditorConfigManager(AppUeditorProperties appUeditorProperties) {
        this.appUeditorProperties = appUeditorProperties;
        this.init();
    }

    private final String rootPath = "config.json";


    private JSONObject jsonConfig = null;


    private static final String SCRAWL_FILE_NAME = "scrawl";
    private static final String REMOTE_FILE_NAME = "remote";


    public void init() {
        // 读取配置文件转换为 json
        try {
            this.jsonConfig = new JSONObject(this.readInputStream());
        } catch (Exception e) {
            this.jsonConfig = null;
        }
    }

    public boolean valid() {
        return this.jsonConfig != null;
    }

    public JSONObject getAllConfig() {
        return this.jsonConfig;
    }

    public Map<String, Object> getConfig(int type) {
        Map<String, Object> conf = new HashMap<>();
        String savePath = null;
        switch (type) {
            case ActionMap.UPLOAD_IMAGE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("imageMaxSize"));
                conf.put("allowFiles", this.getArray("imageAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("imageFieldName"));
                savePath = this.jsonConfig.getString("imagePathFormat");
                break;
            case ActionMap.UPLOAD_SCRAWL:
                conf.put("filename", "scrawl");
                conf.put("maxSize", this.jsonConfig.getLong("scrawlMaxSize"));
                conf.put("fieldName", this.jsonConfig.getString("scrawlFieldName"));
                conf.put("isBase64", "true");
                savePath = this.jsonConfig.getString("scrawlPathFormat");
                break;
            case ActionMap.UPLOAD_VIDEO:
                conf.put("maxSize", this.jsonConfig.getLong("videoMaxSize"));
                conf.put("allowFiles", this.getArray("videoAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("videoFieldName"));
                savePath = this.jsonConfig.getString("videoPathFormat");
                break;
            case ActionMap.UPLOAD_FILE:
                conf.put("isBase64", "false");
                conf.put("maxSize", this.jsonConfig.getLong("fileMaxSize"));
                conf.put("allowFiles", this.getArray("fileAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("fileFieldName"));
                savePath = this.jsonConfig.getString("filePathFormat");
                break;
            case ActionMap.CATCH_IMAGE:
                conf.put("filename", "remote");
                conf.put("filter", this.getArray("catcherLocalDomain"));
                conf.put("maxSize", this.jsonConfig.getLong("catcherMaxSize"));
                conf.put("allowFiles", this.getArray("catcherAllowFiles"));
                conf.put("fieldName", this.jsonConfig.getString("catcherFieldName") + "[]");
                savePath = this.jsonConfig.getString("catcherPathFormat");
                break;
            case ActionMap.LIST_FILE:
                conf.put("allowFiles", this.getArray("fileManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getString("fileManagerListPath"));
                conf.put("count", this.jsonConfig.getInt("fileManagerListSize"));
                break;
            case ActionMap.LIST_IMAGE:
                conf.put("allowFiles", this.getArray("imageManagerAllowFiles"));
                conf.put("dir", this.jsonConfig.getString("imageManagerListPath"));
                conf.put("count", this.jsonConfig.getInt("imageManagerListSize"));
        }

        conf.put("savePath", savePath);
        conf.put("rootPath", appUeditorProperties.getStorePath());
        return conf;
    }

    private String readInputStream() {
        StringBuilder builder = new StringBuilder();
        try (
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(new ClassPathResource(appUeditorProperties.getConfigFile()).getInputStream()));
        ) {
            String tmpContent = null;
            while ((tmpContent = bfReader.readLine()) != null) {
                builder.append(tmpContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.filter(builder.toString());
    }


    private String[] getArray(String key) {
        JSONArray jsonArray = this.jsonConfig.getJSONArray(key);
        String[] result = new String[jsonArray.length()];
        int i = 0;
        for (int len = jsonArray.length(); i < len; ++i) {
            result[i] = jsonArray.getString(i);
        }
        return result;
    }

    private String filter(String input) {
        return input.replaceAll("/\\*[\\s\\S]*?\\*/", "");
    }
}
