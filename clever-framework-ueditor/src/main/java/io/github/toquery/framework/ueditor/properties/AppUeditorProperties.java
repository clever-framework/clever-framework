package io.github.toquery.framework.ueditor.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * ueditor 富文本编辑器配置
 *
 * @author toquery
 * @version 1
 */
@Data
@Component
@ConfigurationProperties(prefix = AppUeditorProperties.PREFIX)
public class AppUeditorProperties {

    /**
     * 配置信息的前缀
     */
    public static final String PREFIX = "app.ueditor";

    /**
     * ueditor 配置文件所在路径
     */
    private String configFile = "ueditor-config.json";

    /**
     * 文件上传参数名称
     */
    private String uploadParam = "upfile";


    /**
     * 文件上传保存路径,默认 相对于项目当前路径，否则设置绝对路径
     */
    private String storePath = System.getProperty("user.dir") + File.separator + "files" + File.separator + "ueditor" + File.separator;

}
