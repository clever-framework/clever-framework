package com.toquery.framework.example.bff.admin.app.controller;

import com.toquery.framework.example.modules.news.info.entity.BizNews;
import com.toquery.framework.example.modules.news.info.service.BizNewsDomainService;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.webmvc.domain.ResponseBody;
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
@RequestMapping("/admin/security")
public class AppSecurityRest extends AppBaseCrudController<BizNewsDomainService, BizNews> {


    // @Filters()
    @Secured({"ROLE_normal", "ROLE_admin"})
    @RequestMapping("/secured")
    public ResponseBody secured() {
        return ResponseBody.builder().content("secured").build();
    }

    // @PreAuthorize("hasAnyRole('normal')")
    @RequestMapping("/normal")
    public ResponseBody normal() {
        return ResponseBody.builder().content("normal").build();
    }

    // @PreAuthorize("hasAnyAuthority('admin')")
    @RequestMapping("/admin")
    public ResponseBody admin() {
        return ResponseBody.builder().content("admin").build();
    }


    // @PreAuthorize("hasAnyAuthority('normal','admin')")
    @RequestMapping("/all")
    public ResponseBody all() {
        return ResponseBody.builder().content("all").build();
    }

}
