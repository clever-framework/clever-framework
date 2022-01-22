package io.github.toquery.framework.core.security;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 *
 */
public class AppSecurityDefaultIgnoring implements AppSecurityIgnoring{
    @Override
    public Set<String> ignoring() {
        return Sets.newHashSet("/actuator/*");
    }
}
