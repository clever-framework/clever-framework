package io.github.toquery.framework.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.toquery.framework.system.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    List<SysRoleMenu> findWithSysRoleMenuByRoleIds(@Param("sysRoleIds") Set<Long> sysRoleIds);
}
