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
import java.io.UnsupportedEncodingException;

/**
 * @author toquery
 * @version 1
 */
public class AppDownloadFileUtil {

    /**
     * 下载文件
     *
     * @param file             文件
     * @param downLoadFileName 下载时显示文件名
     * @param mediaType        文件类型
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> download(String file, String downLoadFileName, String mediaType) {
        ResponseEntity<InputStreamResource> response = null;
        try {
            InputStream inputStream = new FileInputStream(file);
            response = buildInputStream(inputStream, downLoadFileName, mediaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 下载文件
     *
     * @param filePath         文件所在目录
     * @param fileName         文件名
     * @param downLoadFileName 下载时显示文件名
     * @param mediaType        文件类型
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> download(String filePath, String fileName, String downLoadFileName, String mediaType) {
        String path = filePath + File.separator + fileName;
        ResponseEntity<InputStreamResource> response = null;
        try {
            InputStream inputStream = new FileInputStream(path);
            response = buildInputStream(inputStream, downLoadFileName, mediaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /**
     * 下载jar包文件
     *
     * @param filePath         文件所在目录
     * @param fileName         文件名
     * @param downLoadFileName 下载时显示文件名
     * @param mediaType        文件类型
     * @return 响应
     */
    public static ResponseEntity<InputStreamResource> downloadJarFile(String filePath, String fileName, String downLoadFileName, String mediaType) {
        String path = filePath + File.separator + fileName;
        ResponseEntity<InputStreamResource> response = null;
        ClassPathResource classPathResource = new ClassPathResource(path);
        try {
            InputStream inputStream = classPathResource.getInputStream();
            response = buildInputStream(inputStream, downLoadFileName, mediaType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 将文件流转为响应流
     *
     * @param inputStream      文件流
     * @param downLoadFileName 下载时显示文件名
     * @param mediaType        文件类型
     * @return 响应流
     * @throws UnsupportedEncodingException 文件名转换异常
     */
    public static ResponseEntity<InputStreamResource> buildInputStream(InputStream inputStream, String downLoadFileName, String mediaType) throws IOException {
        MediaType mediaTypeEnum = MediaType.parseMediaType(mediaType);
        HttpHeaders headers = new HttpHeaders();
        // headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + new String(downLoadFileName.getBytes("gbk"), "iso8859-1"));
        // headers.add("Pragma", "no-cache");
        // headers.add("Expires", "0");
        return ResponseEntity.ok().headers(headers)
                .contentType(mediaTypeEnum)
                .contentLength(inputStream.available())
                .body(new InputStreamResource(inputStream));
    }
}
