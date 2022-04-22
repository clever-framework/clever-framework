package io.github.toquery.framework.log.service.impl;

import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.dao.primary.snowflake.SnowFlake;
import io.github.toquery.framework.log.entity.SysLog;
import io.github.toquery.framework.log.repository.SysLogMapper;
import io.github.toquery.framework.log.repository.SysLogRepository;
import io.github.toquery.framework.log.service.ISysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author toquery
 * @version 1
 */
public class SysLogServiceImpl extends AppBaseServiceImpl<SysLog, SysLogRepository> implements ISysLogService {

//    @Resource
//    private JdbcTemplate jdbcTemplate;

    @Resource
    private SysLogMapper sysLogMapper;

    @Resource
    private AppUserDetailService userDetailsService;

    private static final Map<String, String> expressions = new LinkedHashMap<String, String>() {
        {
            put("bizName", "bizName:EQ");
            put("logType", "logType:EQ");
            put("moduleName", "moduleName:EQ");
            put("bizNameLIKE", "bizName:LIKE");
            put("moduleNameLIKE", "moduleName:LIKE");
            put("createDateTimeGT", "createDateTime:GTDATE");
            put("createDateTimeLT", "createDateTime:LTDATE");
        }
    };

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressions;
    }


    public int insertSysLog(Long userId, String userName, String moduleName, String bizName, AppLogType logType, String rawData, String targetData) {
        SysLog sysLog = new SysLog();

        sysLog.setModuleName(moduleName);
        sysLog.setBizName(bizName);
        sysLog.setRawData(rawData);
        sysLog.setTargetData(targetData);
        sysLog.setUserId(userId);
        sysLog.setUserName(userName);
        sysLog.setOperateDateTime(LocalDateTime.now());

        if (logType == null) {
            sysLog.setLogType(AppLogType.CREATE);
        }

        LocalDateTime now = LocalDateTime.now();
        sysLog.setCreateDateTime(now);
        sysLog.setUpdateDateTime(now);
        if (userId == null) {
            sysLog.setCreateUserId(0L);
            sysLog.setUpdateUserId(0L);
        } else {
            sysLog.setCreateUserId(userId);
            sysLog.setUpdateUserId(userId);
        }

        sysLog.preInsert();
        sysLog.setId(new SnowFlake().nextId());
        return sysLogMapper.insertSysLog(sysLog);
    }

    @Override
    public Page<SysLog> pageWithUser(Map<String, Object> filterParam, int pageCurrent, int pageSize, String[] pageSort) {
        Page<SysLog> sysLogPage = super.page(filterParam, pageCurrent, pageSize, pageSort);
        List<SysLog> sysLogList = sysLogPage.getContent();
        if (sysLogList.size() > 0) {
            Set<Long> userIds = sysLogList.stream().map(SysLog::getUserId).collect(Collectors.toSet());

            if (userIds.size() > 0) {
                Map<Long, AppUserDetails> userDetailsMap = userDetailsService.userDetailsMap(userIds);
                sysLogList.forEach(sysLog -> {
                    AppUserDetails userDetails = userDetailsMap.get(sysLog.getUserId());
                    sysLog.setSysUser(userDetails);
                });
            }
        }
        return new PageImpl<>(sysLogList, sysLogPage.getPageable(), sysLogPage.getTotalElements());
    }
}
