package io.github.toquery.framework.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.toquery.framework.system.entity.SysUser;
import org.springframework.data.repository.query.Param;

/**
 * @author toquery
 * @version 1
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    public SysUser getByUsername(@Param("username") String username);
}
