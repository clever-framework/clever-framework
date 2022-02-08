package io.github.toquery.framework.minio.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppMinioProperties.PREFIX)
public class AppMinioProperties {

    public static final String PREFIX = "app.oss.minio";

    private boolean enabled = true;

    private String endpoint;
    private int port = 0;
    private String accessKey;
    private String secretKey;
    private String bucket = "clever-framework-minio";

    private AppMinioPathProperties path = new AppMinioPathProperties();



}
