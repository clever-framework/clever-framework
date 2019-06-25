package io.github.toquery.framework.ueditor.upload;


import io.github.toquery.framework.ueditor.PathFormat;
import io.github.toquery.framework.ueditor.define.AppInfo;
import io.github.toquery.framework.ueditor.define.BaseState;
import io.github.toquery.framework.ueditor.define.FileType;
import io.github.toquery.framework.ueditor.define.State;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class BinaryUploader {

    public static final State save(HttpServletRequest request, String uploadFileParamName,String storeFilePath, Map<String, Object> conf) {
        try {
            Part part = request.getPart(uploadFileParamName);

            if (part.getInputStream().available() <= 0) {
                return new BaseState(false, AppInfo.NOTFOUND_UPLOAD_DATA);
            }

            String savePath = (String) conf.get("savePath");
            String originFileName = part.getSubmittedFileName();
            String suffix = FileType.getSuffixByFilename(originFileName);

            originFileName = originFileName.substring(0, originFileName.length() - suffix.length());
            savePath = savePath + suffix;

            long maxSize = (Long) conf.get("maxSize");

            if (!validType(suffix, (String[]) conf.get("allowFiles"))) {
                return new BaseState(false, AppInfo.NOT_ALLOW_FILE_TYPE);
            }

            savePath = PathFormat.parse(savePath, originFileName);

            String physicalPath = conf.get("rootPath") + savePath;

            InputStream is = part.getInputStream();
            State storageState = StorageManager.saveFileByInputStream(is, storeFilePath + savePath.substring(savePath.indexOf("ueditor") + 7), maxSize);
            is.close();

            if (storageState.isSuccess()) {
                storageState.putInfo("url", PathFormat.format(savePath));
                storageState.putInfo("type", suffix);
                storageState.putInfo("original", originFileName + suffix);
            }

            return storageState;
        } catch (IOException | ServletException e) {
            e.printStackTrace();
        }
        return new BaseState(false, AppInfo.IO_ERROR);
    }

    private static boolean validType(String type, String[] allowTypes) {
        List<String> list = Arrays.asList(allowTypes);
        return list.contains(type);
    }
}
