package io.github.toquery.framework.minio.rest;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import io.github.toquery.framework.minio.exception.AppMinioException;
import io.github.toquery.framework.minio.service.AppMinioService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.util.Map;

@Slf4j
@RestController
public class AppMinioRest {

    @Autowired
    private AppMinioService appMinioService;

    /**
     * 上传文件
     *
     * @param uploadParam      上传文件的参数
     * @param bucketName       存储的bucket名称，如果不存在将去新建
     * @param multipartRequest 上传的文件流
     * @param absolutePath     是否使用绝对路径，默认不使用
     * @param domainName       minio服务的域名信息
     * @return 文件存放地址
     * @throws AppMinioException 文件上传异常
     */
    @PostMapping("${app.minio.path.upload:/app/minio/upload}")
    public ResponseEntity upload(@RequestParam(value = "uploadParam", required = false, defaultValue = "${app.minio.path.uploadParam:file}") String uploadParam,
                                 @RequestParam(value = "bucketName", required = false, defaultValue = "${app.minio.bucket:clever-framework-minio}") String bucketName,
                                 @RequestParam(value = "absolutePath", required = false, defaultValue = "true") boolean absolutePath,
                                 @RequestParam(value = "domainName", required = false, defaultValue = "${app.minio.path.domain:}") String domainName,
                                 MultipartRequest multipartRequest) throws AppMinioException {
        MultipartFile file = multipartRequest.getFile(uploadParam);
        if (file == null) {
            return ResponseEntity.badRequest().body("未获取到上传的文件信息！");
        }
        if (absolutePath && Strings.isNullOrEmpty(domainName)) {
            return ResponseEntity.badRequest().body("未获取域名信息！");
        }
        Map<String, String> map = Maps.newHashMap();
        String objectUrl = appMinioService.putObjectAndMakeBucket(bucketName, file);
        map.put("content", absolutePath ? domainName + objectUrl : objectUrl);
        return ResponseEntity.ok().body(map);
    }
}
