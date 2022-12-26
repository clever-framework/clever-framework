package io.github.toquery.framework.log.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.security.userdetails.AppUserDetailService;
import io.github.toquery.framework.core.security.userdetails.AppUserDetails;
import io.github.toquery.framework.log.entity.SysLog;
import io.github.toquery.framework.log.repository.SysLogMapper;
import io.github.toquery.framework.log.service.ISysLogService;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

//    @Resource
//    private JdbcTemplate jdbcTemplate;


    @Resource
    private AppUserDetailService userDetailsService;


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
        return super.baseMapper.insertSysLog(sysLog);
    }

    @Override
    public Page<SysLog> pageWithUser(Map<String, Object> filterParam, int pageCurrent, int pageSize, String[] pageSort) {
        Page<SysLog> sysLogPage = super.page(new Page<>(pageCurrent, pageSize));
        List<SysLog> sysLogList = sysLogPage.getRecords();
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
        return sysLogPage;
    }
}
