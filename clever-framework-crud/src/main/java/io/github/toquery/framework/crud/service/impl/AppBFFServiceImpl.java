package io.github.toquery.framework.crud.service.impl;

import io.github.toquery.framework.crud.service.AppBFFService;

import java.util.Map;

/**
 *
 */
public abstract class AppBFFServiceImpl implements AppBFFService {

    protected abstract Map<String, String> getQueryExpressions();
}
