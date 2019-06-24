package io.github.toquery.framework.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class FileType {
    public static final String JPG = "JPG";
    private static final Map<String, String> types = new HashMap<String, String>() {
        {
            this.put("JPG", ".jpg");
        }
    };

    public FileType() {
    }

    public static String getSuffix(String key) {
        return types.get(key);
    }

    public static String getSuffixByFilename(String filename) {
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }
}
