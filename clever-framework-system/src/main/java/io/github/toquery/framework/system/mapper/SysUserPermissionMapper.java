package io.github.toquery.framework.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.toquery.framework.system.entity.SysUserPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 */
@Mapper
public interface SysUserPermissionMapper extends BaseMapper<SysUserPermission> {

    List<SysUserPermission> findWithFullByUserId(@Param("userId") Long userId);
}
