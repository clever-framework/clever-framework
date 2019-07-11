package io.github.toquery.framework.files.properties;

import io.github.toquery.framework.common.util.AppUtilDate;
import lombok.Data;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;
import java.io.IOException;

/**
 * @author toquery
 * @version 1
 */
@Data
@ConfigurationProperties(prefix = AppFilesProperties.PREFIX)
public class AppFilesProperties {

    /**
     * 配置信息的前缀
     */
    public static final String PREFIX = "app.files";

    /**
     * 文件上传参数名称
     */
    private String uploadParam = "file";

    private AppFilesPath path = new AppFilesPath();

    @Data
    public class AppFilesPath {
        /**
         * 文件上传保存路径,默认相对于项目当前路径，否则设置绝对路径
         */
        private String store = System.getProperty("user.dir") + File.separator + "files" + File.separator + "biz" + File.separator;

        /**
         *
         */
        private String upload = "/app/files/upload";

        private String download = "/app/files/download";

        public String getStoreWithDate() {

            String pathWithDate = getStore() + AppUtilDate.getCurrentDate() + File.separator;

            // 创建文件目录
            try {
                FileUtils.forceMkdir(new File(pathWithDate));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return pathWithDate;
        }
    }

}
