package io.github.toquery.framework.ueditor.define;

import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
public class AppInfo {
    public static final int SUCCESS = 0;
    public static final int MAX_SIZE = 1;
    public static final int PERMISSION_DENIED = 2;
    public static final int FAILED_CREATE_FILE = 3;
    public static final int IO_ERROR = 4;
    public static final int NOT_MULTIPART_CONTENT = 5;
    public static final int PARSE_REQUEST_ERROR = 6;
    public static final int NOTFOUND_UPLOAD_DATA = 7;
    public static final int NOT_ALLOW_FILE_TYPE = 8;
    public static final int INVALID_ACTION = 101;
    public static final int CONFIG_ERROR = 102;
    public static final int PREVENT_HOST = 201;
    public static final int CONNECTION_ERROR = 202;
    public static final int REMOTE_FAIL = 203;
    public static final int NOT_DIRECTORY = 301;
    public static final int NOT_EXIST = 302;
    public static final int ILLEGAL = 401;


    public static Map<Integer, String> info = new HashMap<Integer, String>() {
        {
            this.put(SUCCESS, "SUCCESS");
            this.put(INVALID_ACTION, "无效的Action");
            this.put(CONFIG_ERROR, "配置文件初始化失败");
            this.put(REMOTE_FAIL, "抓取远程图片失败");
            this.put(PREVENT_HOST, "被阻止的远程主机");
            this.put(CONNECTION_ERROR, "远程连接出错");
            this.put(MAX_SIZE, "文件大小超出限制");
            this.put(PERMISSION_DENIED, "权限不足");
            this.put(FAILED_CREATE_FILE, "创建文件失败");
            this.put(IO_ERROR, "IO错误");
            this.put(NOT_MULTIPART_CONTENT, "上传表单不是multipart/form-data类型");
            this.put(PARSE_REQUEST_ERROR, "解析上传表单错误");
            this.put(NOTFOUND_UPLOAD_DATA, "未找到上传数据");
            this.put(NOT_ALLOW_FILE_TYPE, "不允许的文件类型");
            this.put(NOT_DIRECTORY, "指定路径不是目录");
            this.put(NOT_EXIST, "指定路径并不存在");
            this.put(ILLEGAL, "Callback参数名不合法");
        }
    };

    public AppInfo() {
    }

    public static String getStateInfo(int key) {
        return info.get(key);
    }
}
