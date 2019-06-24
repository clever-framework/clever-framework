package io.github.toquery.framework.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class MIMEType {
    public static final Map<String, String> types = new HashMap<String, String>() {
        {
            this.put("image/gif", ".gif");
            this.put("image/jpeg", ".jpg");
            this.put("image/jpg", ".jpg");
            this.put("image/png", ".png");
            this.put("image/bmp", ".bmp");
        }
    };

    public MIMEType() {
    }

    public static String getSuffix(String mime) {
        return types.get(mime);
    }
}
