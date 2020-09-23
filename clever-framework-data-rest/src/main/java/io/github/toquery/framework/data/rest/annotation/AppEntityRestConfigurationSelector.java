package io.github.toquery.framework.data.rest.annotation;

import io.github.toquery.framework.data.rest.AppEntityRestScannerRegistrar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachingConfigurationSelector;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.AdviceModeImportSelector;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @see EnableAppEntityRest
 * @see AppEntityRestScannerRegistrar
 * <p>
 * 示例代码
 * @see org.springframework.cache.annotation.CachingConfigurationSelector
 * </p>
 */
@Slf4j
public class AppEntityRestConfigurationSelector extends AdviceModeImportSelector<EnableAppEntityRest> {

    public AppEntityRestConfigurationSelector() {
        log.debug(this.getClass().getName());
    }

    @Override
    protected String[] selectImports(AdviceMode adviceMode) {
        log.debug("App Entity Rest Selector 代理模式 {}", adviceMode);
        switch (adviceMode) {
            case PROXY:
                return getProxyImports();
            case ASPECTJ:
                return getAspectJImports();
            default:
                return null;
        }
    }

    /**
     * Return the imports to use if the {@link AdviceMode} is set to {@link AdviceMode#PROXY}.
     * <p>Take care of adding the necessary JSR-107 import if it is available.
     */
    private String[] getProxyImports() {
        List<String> result = new ArrayList<>(3);
        result.add(AppEntityRestScannerRegistrar.class.getName());
        return StringUtils.toStringArray(result);
    }

    /**
     * todo
     * Return the imports to use if the {@link AdviceMode} is set to {@link AdviceMode#ASPECTJ}.
     * <p>Take care of adding the necessary JSR-107 import if it is available.
     */
    private String[] getAspectJImports() {
        return null;
    }


}
