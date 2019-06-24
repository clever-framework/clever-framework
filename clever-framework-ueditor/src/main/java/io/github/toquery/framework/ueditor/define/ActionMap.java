package io.github.toquery.framework.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class ActionMap {
    public static final Map<String, Integer> mapping = new HashMap<String, Integer>() {
        {
            this.put("config", CONFIG);
            this.put("uploadimage", UPLOAD_IMAGE);
            this.put("uploadscrawl", UPLOAD_SCRAWL);
            this.put("uploadvideo", UPLOAD_VIDEO);
            this.put("uploadfile", UPLOAD_FILE);
            this.put("catchimage", CATCH_IMAGE);
            this.put("listfile", LIST_FILE);
            this.put("listimage", LIST_IMAGE);
        }
    };
    public static final int CONFIG = 0;
    public static final int UPLOAD_IMAGE = 1;
    public static final int UPLOAD_SCRAWL = 2;
    public static final int UPLOAD_VIDEO = 3;
    public static final int UPLOAD_FILE = 4;
    public static final int CATCH_IMAGE = 5;
    public static final int LIST_FILE = 6;
    public static final int LIST_IMAGE = 7;

    public ActionMap() {
    }

    public static int getType(String key) {
        return mapping.get(key);
    }
}
