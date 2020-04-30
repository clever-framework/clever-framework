package io.github.toquery.framework.minio.service;

import com.google.common.io.Files;
import io.github.toquery.framework.minio.exception.AppMinioException;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import io.minio.ServerSideEncryption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Map;
import java.util.UUID;

public class AppMinioService {
//    @Autowired
//    private AppMinioProperties appMinioProperties;

    @Autowired
    private MinioClient minioClient;

    /**
     * 判断bucketName是否存在
     *
     * @param bucketName bucketName
     * @return
     * @throws AppMinioException
     */
    public boolean bucketExists(String bucketName) throws AppMinioException {
        boolean bucketExists;
        try {
            bucketExists = minioClient.bucketExists(bucketName);
        } catch (Exception e) {
            throw new AppMinioException(e);
        }
        return bucketExists;
    }

    /**
     * 创建 bucketName
     *
     * @param bucketName bucketName
     * @throws AppMinioException
     */
    public void makeBucket(String bucketName) throws AppMinioException {
        try {
            minioClient.makeBucket(bucketName);
        } catch (Exception e) {
            throw new AppMinioException(e);
        }
    }

    /**
     * 检查bucketName是否存在，不存在则去创建一个
     *
     * @param bucketName bucketName
     * @throws AppMinioException
     */
    public void makeBucketAndCheck(String bucketName) throws AppMinioException {
        boolean bucketExists = this.bucketExists(bucketName);
        if (!bucketExists) {
            this.makeBucket(bucketName);
        }
    }

    /**
     * 获取 bucketName 的 objectName 的url路径但返回的是minio的endpoint地址
     *
     * @param bucketName bucketName
     * @param objectName objectName
     * @return
     * @throws AppMinioException
     */
    public String getObjectUrl(String bucketName, String objectName) throws AppMinioException {
        try {
            return minioClient.getObjectUrl(bucketName, objectName);
        } catch (Exception e) {
            throw new AppMinioException(e);
        }
    }

    /**
     * 提交文件对象
     *
     * @param bucketName
     * @param file
     * @throws AppMinioException
     */
    public String putObjectAndMakeBucket(String bucketName, MultipartFile file) throws AppMinioException {
        try {
            InputStream fileInputStream = file.getInputStream();
            String originalFilename = file.getOriginalFilename();
            String newFileName = UUID.randomUUID().toString() + "." + Files.getFileExtension(originalFilename);
            //Map<String, String> headerMap = new HashMap<>();
            //headerMap.put("originalFilename", originalFilename);
            this.makeBucketAndCheck(bucketName);
            this.putObject(bucketName, newFileName, fileInputStream, file.getSize(), null, null, file.getContentType());
            return "/" + bucketName + "/" + newFileName;
        } catch (Exception e) {
            throw new AppMinioException(e);
        }
    }

    /**
     * @param bucketName  存储桶名称
     * @param objectName  存储桶里的对象名称
     * @param stream      InputStream	要上传的流
     * @param size        要上传的stream的size
     * @param headerMap
     * @param sse
     * @param contentType Content type
     * @throws AppMinioException
     */
    public void putObject(String bucketName, String objectName, InputStream stream, Long size,
                          Map<String, String> headerMap, ServerSideEncryption sse, String contentType) throws AppMinioException {
        try {
            // todo 临时配置
            PutObjectOptions putObjectOptions = new PutObjectOptions(size, -1);
            putObjectOptions.setContentType(contentType);
            putObjectOptions.setSse(sse);
            putObjectOptions.setHeaders(headerMap);
            minioClient.putObject(bucketName, objectName, stream, putObjectOptions);
        } catch (Exception e) {
            throw new AppMinioException(e);
        }
    }

    /*
    public String getBucketPolicy(String bucketName) throws AppMinioException {
        if (this.bucketExists(bucketName)) {
            return minioClient.getBucketPolicy(bucketName);
        }
        return null;
    }


    public void setBucketPolicy(String bucketName, String objectPrefix, PolicyType policyType) throws AppMinioException {
        if (this.bucketExists(bucketName)) {
            minioClient.setBucketPolicy(bucketName, objectPrefix, policyType);
        } else {

        }
    }
    */
}
