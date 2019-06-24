package io.github.toquery.framework.common.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author toquery
 * @version 1
 */
public class DownloadFileUtil {

    public static ResponseEntity<InputStreamResource> download(String file, String newName) {
        ResponseEntity<InputStreamResource> response = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            response = buildInputStream(inputStream, newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 下载样表
     *
     * @param filePath 文件上级目录
     * @param fileName 文件名
     * @param newName  下载的展示文件名
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> download(String filePath, String fileName, String newName) {
        String path = filePath + File.separator + fileName;
        ResponseEntity<InputStreamResource> response = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            response = buildInputStream(inputStream, newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public static ResponseEntity<InputStreamResource> downloadJarFile(String filePath, String fileName, String newName) {
        String path = filePath + File.separator + fileName;
        ResponseEntity<InputStreamResource> response = null;
        ClassPathResource classPathResource = new ClassPathResource(path);
        try {
            InputStream inputStream = classPathResource.getInputStream();
            response = buildInputStream(inputStream, newName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    public static ResponseEntity<InputStreamResource> buildInputStream(InputStream inputStream, String newName) {
        ResponseEntity<InputStreamResource> response = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + new String(newName.getBytes("gbk"), "iso8859-1"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
            response = ResponseEntity.ok().headers(headers)
                    .contentType(MediaType.parseMediaType("application/octet-stream"))
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
