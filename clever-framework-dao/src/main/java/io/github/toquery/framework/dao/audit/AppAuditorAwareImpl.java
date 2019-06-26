package io.github.toquery.framework.dao.audit;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * @author toquery
 * @version 1
 */
public class AppAuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(1L);
    }

}
