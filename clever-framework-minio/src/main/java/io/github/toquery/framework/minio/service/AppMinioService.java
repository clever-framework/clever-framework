package io.github.toquery.framework.minio.service;

import io.minio.MinioClient;

import java.util.Map;

public class AppMinioService {

    private MinioClient minioClient;

    public AppMinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(bucketName);
    }

    /*public Map<String, PolicyType> getBucketPolicy(String bucketName) throws Exception {
        if (this.bucketExists(bucketName)) {
            return minioClient.getBucketPolicy(bucketName);
        }
        return null;
    }


    public void getBucketPolicy(String bucketName, String objectPrefix, PolicyType policyType) throws Exception {
        if (this.bucketExists(bucketName)) {
            minioClient.setBucketPolicy(bucketName, objectPrefix, policyType);
        } else {

        }
    }*/
}
