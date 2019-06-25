package io.github.toquery.framework.ueditor.upload;


import io.github.toquery.framework.ueditor.PathFormat;
import io.github.toquery.framework.ueditor.define.AppInfo;
import io.github.toquery.framework.ueditor.define.BaseState;
import io.github.toquery.framework.ueditor.define.FileType;
import io.github.toquery.framework.ueditor.define.State;

import java.util.Base64;
import java.util.Map;


public final class Base64Uploader {

    public static State save(String content, Map<String, Object> conf) {

        byte[] data = decode(content);

        long maxSize = (Long) conf.get("maxSize");

        if (!validSize(data, maxSize)) {
            return new BaseState(false, AppInfo.MAX_SIZE);
        }

        String suffix = FileType.getSuffix("JPG");

        String savePath = PathFormat.parse((String) conf.get("savePath"), (String) conf.get("filename"));

        savePath = savePath + suffix;
        String physicalPath = conf.get("rootPath") + savePath;

        State storageState = StorageManager.saveBinaryFile(data, physicalPath);

        if (storageState.isSuccess()) {
            storageState.putInfo("url", PathFormat.format(savePath));
            storageState.putInfo("type", suffix);
            storageState.putInfo("original", "");
        }

        return storageState;
    }

    private static byte[] decode(String content) {
        return Base64.getDecoder().decode(content);
    }

    private static boolean validSize(byte[] data, long length) {
        return data.length <= length;
    }

}