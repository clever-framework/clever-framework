package com.toquery.framework.example.bff.admin.app.rest;

import com.toquery.framework.example.modules.news.entity.BizNews;
import com.toquery.framework.example.modules.news.service.BizNewsDomainService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import org.springframework.security.access.annotation.Secured;
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
public class AppSecurityRest extends AppBaseCrudController<BizNewsDomainService, BizNews> {


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

    // @PreAuthorize("hasAnyAuthority('admin')")
    @RequestMapping("/admin")
    public ResponseParam admin() {
        return ResponseParam.builder().content("admin").build();
    }


    // @PreAuthorize("hasAnyAuthority('normal','admin')")
    @RequestMapping("/all")
    public ResponseParam all() {
        return ResponseParam.builder().content("all").build();
    }

}