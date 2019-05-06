package io.github.toquery.framework.security.rest;

import io.github.toquery.framework.security.domain.SysUser;
import io.github.toquery.framework.security.service.ISysUserService;
import io.github.toquery.framework.webmvc.AppBaseController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/sys/user")
public class SystemUserRest extends AppBaseController<ISysUserService> {


//    @Resource
//    private ISysUserService sysUserService;

    //@GetMapping
   // public void list() {
    //    sysUserService.find();
   // }

    @PostMapping
    public void save(SysUser sysUser) {
    }

    @PutMapping
    public void update(SysUser sysUser) {

    }

    @DeleteMapping("{id}")
    public void delete() {

    }

    @GetMapping("{id}")
    public void detail() {

    }
}
