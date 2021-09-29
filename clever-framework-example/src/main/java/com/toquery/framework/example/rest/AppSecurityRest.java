package com.toquery.framework.example.rest;

import com.toquery.framework.example.entity.BizNews;
import com.toquery.framework.example.service.IBizNewsService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * hasAnyRole 会填充 ROLE_
 *
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/app/security")
public class AppSecurityRest extends AppBaseCrudController<IBizNewsService, BizNews> {


    // @Filters()
    @Secured({"ROLE_normal", "ROLE_admin"})
    @RequestMapping("/secured")
    public ResponseParam secured() {
        return ResponseParam.builder().content("secured").build();
    }

    // @PreAuthorize("hasAnyRole('normal')")
    @RequestMapping("/normal")
    public ResponseParam normal() {
        return ResponseParam.builder().content("normal").build();
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @RequestMapping("/admin")
    public ResponseParam admin() {
        return ResponseParam.builder().content("admin").build();
    }


    @PreAuthorize("hasAnyAuthority('normal','admin')")
    @RequestMapping("/all")
    public ResponseParam all() {
        return ResponseParam.builder().content("all").build();
    }

}
