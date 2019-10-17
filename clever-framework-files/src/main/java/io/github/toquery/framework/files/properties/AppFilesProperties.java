package io.github.toquery.framework.files.properties;

import io.github.toquery.framework.common.util.AppDateUtil;
import io.github.toquery.framework.files.constant.AppFileStoreTypeEnum;
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

    /**
     * 文件存储方式
     */
    private AppFileStoreTypeEnum storeType = AppFileStoreTypeEnum.DATABASE;

    // 展示用的地址，只有在 storeType = FILE 时使用，不填写时将使用当前访问的域名的相对路径
    private String showDomain = "";


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

            String pathWithDate = getStore() + AppDateUtil.getCurrentDate() + File.separator;

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
