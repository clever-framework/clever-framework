package io.github.toquery.framework.dao.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author toquery
 * @version 1
 */
public class AppAuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // your custom logic
        return Optional.of("admin");
    }

}
