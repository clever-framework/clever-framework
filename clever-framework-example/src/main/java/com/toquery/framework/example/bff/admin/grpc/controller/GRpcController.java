package com.toquery.framework.example.bff.admin.grpc.controller;

import com.toquery.framework.example.bff.admin.news.info.model.constant.QueryType;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsAddRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsListRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsUpdateRequest;
import com.toquery.framework.example.sdk.news.info.BizNewInfoClient;
import com.toquery.framework.example.sdk.news.info.model.request.BizNewsPageRequest;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
import io.github.toquery.framework.web.domain.ResponseBody;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Set;

/**
 *
 */
@RestController
@RequestMapping("/admin/grpc")
public class GRpcController {

    @Resource
    private BizNewInfoClient bizNewInfoClient;


    @GetMapping
    public ResponseBody page(BizNewsPageRequest bizNewsPageRequest) {
        return bizNewInfoClient.page(bizNewsPageRequest);
    }


    @GetMapping("/list")
    public ResponseBody list(@RequestParam BizNewsListRequest bizNewsListRequest) {
        return null;
    }


    @PostMapping
    public ResponseBody save(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @Validated @RequestBody BizNewsAddRequest request) {
        return null;
    }

    @PutMapping
    public ResponseBody update(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestBody BizNewsUpdateRequest request) {
        return null;
    }


    @DeleteMapping
    public void delete(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestParam Set<Long> ids) {
    }

    @GetMapping("{id}")
    public ResponseBody detail(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @PathVariable Long id) {
        return null;
    }
}
