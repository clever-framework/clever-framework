package io.github.toquery.framework.minio.service;

import io.minio.MinioClient;
import io.minio.errors.*;
import io.minio.policy.PolicyType;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

public class MinioService {

    private MinioClient minioClient;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public boolean bucketExists(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException {
        return minioClient.bucketExists(bucketName);
    }

    public Map<String, PolicyType> getBucketPolicy(String bucketName) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidObjectPrefixException {
        if (this.bucketExists(bucketName)) {
            return minioClient.getBucketPolicy(bucketName);
        }
        return null;
    }


    public void getBucketPolicy(String bucketName, String objectPrefix, PolicyType policyType) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, InternalException, NoResponseException, InvalidBucketNameException, XmlPullParserException, ErrorResponseException, InvalidObjectPrefixException {
        if (this.bucketExists(bucketName)) {
            minioClient.setBucketPolicy(bucketName, objectPrefix, policyType);
        } else {

        }
    }
}
