package io.github.toquery.framework.minio.properties;

import lombok.Data;

@Data
public class AppMinioPathProperties {

    private String upload = "/app/minio/upload";

    private String uploadParam = "file";

    private String domain = "";
    // private String view = "/app/minio/upload";
}
