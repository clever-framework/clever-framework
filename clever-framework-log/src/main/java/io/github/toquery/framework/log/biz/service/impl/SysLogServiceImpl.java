package io.github.toquery.framework.log.biz.service.impl;

import com.github.pagehelper.Page;
import io.github.toquery.framework.log.biz.dao.SysLogRepository;
import io.github.toquery.framework.log.biz.entity.SysLog;
import io.github.toquery.framework.log.biz.service.ISysLogService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@Service
public class SysLogServiceImpl implements ISysLogService {

    @Resource
    private SysLogRepository sysDaoRepository;

    @Override
    public Page<SysLog> handleQuery() {
//        return sysDaoRepository.query();
        return null;
    }

    @Override
    public SysLog save(SysLog sysLog) {
        return sysDaoRepository.save(sysLog);
    }

    @Override
    public SysLog getById(Long id) {
        return sysDaoRepository.getOne(id);
    }
}
