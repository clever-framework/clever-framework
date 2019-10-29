package io.github.toquery.framework.security.auditor;

import io.github.toquery.framework.system.domain.AppBaseAuditedEntity;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.service.ISysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author toquery
 * @version 1
 */
@Service
public class AppAuditedHandle {

    @Resource
    private ISysUserService sysUserService;


    public <E extends AppBaseAuditedEntity> List<E> handleSystemAudited(List<E> list) {
        Set<Long> createIds = list.stream().map(AppBaseAuditedEntity::getCreateUserId).collect(Collectors.toSet());
        Set<Long> updateIds = list.stream().map(AppBaseAuditedEntity::getLastUpdateUserId).collect(Collectors.toSet());
        createIds.addAll(updateIds);
        List<SysUser> sysUserList = sysUserService.findByIds(createIds);
        Map<Long, SysUser> map = sysUserList.stream().collect(Collectors.toMap(SysUser::getId, item -> item));

        list.forEach(item -> {
            item.setCreateUser(map.get(item.getCreateUserId()));
            item.setUpdateUser(map.get(item.getLastUpdateUserId()));
        });

        return list;
    }

}
