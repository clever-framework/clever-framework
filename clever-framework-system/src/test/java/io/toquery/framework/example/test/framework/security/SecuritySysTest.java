package io.toquery.framework.example.test.framework.security;

import com.google.common.collect.Lists;
import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.system.entity.SysMenu;
import io.github.toquery.framework.system.entity.SysRole;
import io.github.toquery.framework.system.entity.SysUser;
import io.github.toquery.framework.system.mapper.SysMenuMapper;
import io.github.toquery.framework.system.mapper.SysRoleMapper;
import io.github.toquery.framework.system.mapper.SysUserMapper;
import io.toquery.framework.test.AppTestSpringBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class SecuritySysTest extends AppTestSpringBase {
    @Resource
    private SysUserMapper sysUserDao;

    @Resource
    private SysMenuMapper sysMenuDao;
    @Resource
    private SysRoleMapper sysRoleDao;

    @Test
    public void insertData() {
//        SysUser sysUser = new SysUser();
//
//        SysMenu sysMenu11 = new SysMenu("1-1", "1-1");
//        SysMenu sysMenu12 = new SysMenu("1-2", "1-2");
//        List<SysMenu> sysMenus1 = Lists.newArrayList(sysMenu11, sysMenu12);
//        sysMenus1 = sysMenuDao.saveAll(sysMenus1);
//
//        SysRole sysRole1 = new SysRole();
//        sysRole1.setMenus(sysMenus1);
//        sysRole1.setRoleName("1");
//        sysRole1 = sysRoleDao.saveAndFlush(sysRole1);
//
//
//        SysMenu sysMenu21 = new SysMenu("2-1", "2-1");
//        SysMenu sysMenu22 = new SysMenu("2-2", "2-2");
//        List<SysMenu> sysMenus2 = Lists.newArrayList(sysMenu21, sysMenu22);
//        sysMenus2 = sysMenuDao.saveAll(sysMenus2);
//
//        SysRole sysRole2 = new SysRole();
//        sysRole2.setMenus(sysMenus2);
//        sysRole2.setRoleName("2");
//        sysRole2 = sysRoleDao.saveAndFlush(sysRole2);
//
//
//        // sysUser.setAuthorities(Sets.newHashSet(sysRole1, sysRole2));
//        sysUser.setEmail("1@qq.com");
//        sysUser.setUsername("1111");
//        sysUser.setPassword("1111");
//        sysUserDao.saveAndFlush(sysUser);
    }

    @Test
    public void findAllSysUserDao() {
//        List<SysUser> sysUsers = sysUserDao.findAll();
//        log.info(JacksonUtils.object2String(sysUsers));
    }


    @Test
    public void findAllSysMenuDao() {
//        List<SysMenu> sysMenus = sysMenuDao.findAll();
//        log.info(JacksonUtils.object2String(sysMenus));
    }

    @Test
    public void findAllSysRoleDao() {
//        List<SysRole> sysRoles = sysRoleDao.findAll();
//        log.info(JacksonUtils.object2String(sysRoles));
    }
}
