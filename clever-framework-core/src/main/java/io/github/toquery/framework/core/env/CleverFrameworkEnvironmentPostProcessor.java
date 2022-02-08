package io.github.toquery.framework.core.env;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Map;

/**
 * 增加 clever-framework.version 环境变量设置,用于输出 banner clever framework version
 *
 * @author toquery
 * @version 1
 * @see org.springframework.boot.ResourceBanner
 */
@Slf4j
public class CleverFrameworkEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    public CleverFrameworkEnvironmentPostProcessor() {
        log.debug("自动装配 App Core Environment Post Processor 模块");
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {

        String version = getCleverFrameworkVersion();
        Map<String, Object> map = environment.getSystemProperties();
        map.put("clever-framework.version", this.getVersionString(version, false));
        map.put("clever-framework.formatted-version", this.getVersionString(version, true));

        MutablePropertySources mutablePropertySources = environment.getPropertySources();
        mutablePropertySources.get("version");
    }

    private String getCleverFrameworkVersion() {
        return CleverFrameworkVersion.getVersion();
    }

    private String getVersionString(String version, boolean format) {
        if (version == null) {
            return "";
        }
        return format ? " (v" + version + ")" : version;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
