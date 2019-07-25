package io.github.toquery.framework.log.domain;

import io.github.toquery.framework.system.domain.SysLog;
import io.github.toquery.framework.system.domain.SysUser;
import lombok.Getter;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Setter
@Getter
public class SysLogVo extends SysLog {

    public SysLogVo(SysLog sysLog, SysUser sysUser) {
        super.setModuleName(sysLog.getModuleName());
        super.setBizName(sysLog.getBizName());
        super.setLogType(sysLog.getLogType());
        super.setRawData(sysLog.getRawData());
        super.setTargetData(sysLog.getTargetData());
        super.setCreateDatetime(sysLog.getCreateDatetime());
        this.sysUser = sysUser;
    }

    private SysUser sysUser;
}
