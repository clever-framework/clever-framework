package io.github.toquery.framework.system.service.impl;

import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.system.entity.SysUserOnline;
import io.github.toquery.framework.system.repository.SysUserOnlineRepository;
import io.github.toquery.framework.system.service.ISysUserOnlineService;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class SysUserOnlineServiceImpl extends AppBaseServiceImpl<SysUserOnline, SysUserOnlineRepository> implements ISysUserOnlineService {


    public SysUserOnlineServiceImpl() {
    }

    @Override
    public Map<String, String> getQueryExpressions() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    @Override
    public void deleteExpires() {
        Instant expiresDate = Instant.now();
        super.repository.deleteByExpiresDate(expiresDate);
    }
}
