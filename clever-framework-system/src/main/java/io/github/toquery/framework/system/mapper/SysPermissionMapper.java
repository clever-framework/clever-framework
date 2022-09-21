package io.github.toquery.framework.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.toquery.framework.system.entity.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    List<SysPermission> findWithFullByUserId(@Param("userId") Long userId);
}
