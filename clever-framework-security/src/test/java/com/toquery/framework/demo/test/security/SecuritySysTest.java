package com.toquery.framework.demo.test.security;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.github.toquery.framework.security.domain.SysMenu;
import io.github.toquery.framework.security.domain.SysRole;
import io.github.toquery.framework.security.domain.SysUser;
import com.toquery.framework.demo.test.BaseSpringTest;
import io.github.toquery.framework.security.repository.SysMenuRepository;
import io.github.toquery.framework.security.repository.SysRoleRepository;
import io.github.toquery.framework.security.repository.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class SecuritySysTest extends BaseSpringTest {
    @Resource
    private SysUserRepository sysUserDao;

    @Resource
    private SysMenuRepository sysMenuDao;
    @Resource
    private SysRoleRepository sysRoleDao;

    @Test
    public void insertData() {
        SysUser sysUser = new SysUser();

        SysMenu sysMenu11 = new SysMenu("1-1", "1-1");
        SysMenu sysMenu12 = new SysMenu("1-2", "1-2");
        List<SysMenu> sysMenus1 = Lists.newArrayList(sysMenu11, sysMenu12);
        sysMenus1 = sysMenuDao.saveAll(sysMenus1);

        SysRole sysRole1 = new SysRole();
        sysRole1.setMenus(sysMenus1);
        sysRole1.setName("1");
        sysRole1 = sysRoleDao.saveAndFlush(sysRole1);


        SysMenu sysMenu21 = new SysMenu("2-1", "2-1");
        SysMenu sysMenu22 = new SysMenu("2-2", "2-2");
        List<SysMenu> sysMenus2 = Lists.newArrayList(sysMenu21, sysMenu22);
        sysMenus2 = sysMenuDao.saveAll(sysMenus2);

        SysRole sysRole2 = new SysRole();
        sysRole2.setMenus(sysMenus2);
        sysRole2.setName("2");
        sysRole2 = sysRoleDao.saveAndFlush(sysRole2);


        sysUser.setRoles(Sets.newHashSet(sysRole1, sysRole2));
        sysUser.setEmail("1@qq.com");
        sysUser.setLoginName("1");
        sysUser.setUserName("1111");
        sysUser.setPassword("1111");
        sysUserDao.saveAndFlush(sysUser);
    }

    @Test
    public void findAllSysUserDao() {
        List<SysUser> sysUsers = sysUserDao.findAll();
        log.info(JSON.toJSONString(sysUsers));
    }


    @Test
    public void findAllSysMenuDao() {
        List<SysMenu> sysMenus = sysMenuDao.findAll();
        log.info(JSON.toJSONString(sysMenus));
    }

    @Test
    public void findAllSysRoleDao() {
        List<SysRole> sysRoles = sysRoleDao.findAll();
        log.info(JSON.toJSONString(sysRoles));
    }
}
