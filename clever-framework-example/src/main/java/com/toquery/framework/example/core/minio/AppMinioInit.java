package com.toquery.framework.example.core.minio;

//import io.minio.policy.PolicyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AppMinioInit {

    //@Resource
    // MinioClient minioClient;

//    @Resource
//    private AppMinioService appMinioService;

    public static final String BUCKET_NAME = "clever-framework-demo";

    public static final String OBJECT_NAME = "123.mov";


    @EventListener(classes = ApplicationStartedEvent.class)
    public void onApplicationEvent(ApplicationStartedEvent event) throws Exception {
        /*
        log.info("AppMinioInit ==> onApplicationEvent method");
        boolean bucketExists = minioClient.bucketExists(BUCKET_NAME);
        log.info("检查 minio 是否存在 {} 为 {}", BUCKET_NAME, bucketExists);
        if (!bucketExists) {
            minioClient.makeBucket(BUCKET_NAME);
        }
        String objectUrl = minioClient.getObjectUrl(BUCKET_NAME, OBJECT_NAME);
        String getBucketPolicy = minioClient.getBucketPolicy(BUCKET_NAME);

        log.info("检查 minio 服务 {} 的 PolicyType 类型： getBucketPolicy {} , {}", BUCKET_NAME, getBucketPolicy, objectUrl);
        */
        // map.forEach((k, v) -> log.info("key = {} , value = {}", k, v));
    }
}
