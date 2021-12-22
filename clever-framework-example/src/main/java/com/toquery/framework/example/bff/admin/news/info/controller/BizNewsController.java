package com.toquery.framework.example.bff.admin.news.info.controller;

import com.toquery.framework.example.bff.admin.news.info.dao.BizNewsDao;
import com.toquery.framework.example.bff.admin.news.info.model.constant.QueryType;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsAddRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsListRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsUpdateRequest;
import com.toquery.framework.example.bff.admin.news.info.model.response.BizNewsInfoResponse;
import com.toquery.framework.example.bff.admin.news.info.model.response.BizNewsListResponse;
import com.toquery.framework.example.bff.admin.news.info.service.BizNewsService;
import com.toquery.framework.example.modules.news.info.entity.BizNews;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseBFFController;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import org.springframework.data.domain.Page;
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

import java.util.List;
import java.util.Set;

/**
 * 新闻
 *
 * @author toquery
 * @version 1
 */
@RestController
@RequestMapping("/admin/biz-news")
public class BizNewsController extends AppBaseBFFController<BizNews, BizNewsService, BizNewsDao> {

    public static final String MODEL_NAME = "新闻管理";

    public static final String BIZ_NAME = "新闻管理";

    public BizNewsController(BizNewsService bffService) {
        super(bffService);
    }

    @GetMapping
    public ResponseResult page(BizNewsPageRequest bizNewsPageRequest) {
        Page<BizNews> bizNewsPage = bffService.page(bizNewsPageRequest);
        return ResponseResult.builder().page(bizNewsPage).build();
    }

    @GetMapping("/multi")
    public ResponseResult pageMultiType(BizNewsPageRequest bizNewsPageRequest) {
        return bffService.pageMultiType(bizNewsPageRequest);
    }

    @GetMapping("/list")
    public ResponseResult list(@RequestParam BizNewsListRequest bizNewsListRequest) {
        List<BizNewsListResponse> listResponses = bffService.list(bizNewsListRequest);
        return super.handleResponseBody(listResponses);
    }


    @AppLogMethod(value = BizNews.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseResult save(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @Validated @RequestBody BizNewsAddRequest request) {
        BizNewsInfoResponse bizNewsInfoResponse = bffService.save(queryType, request);
        return super.handleResponseBody(bizNewsInfoResponse);
    }

    @AppLogMethod(value = BizNews.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseResult update(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestBody BizNewsUpdateRequest request) {

        BizNewsInfoResponse bizNewsInfoResponse = bffService.update(queryType, request);
        return super.handleResponseBody(bizNewsInfoResponse);
    }


    @AppLogMethod(value = BizNews.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void delete(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestParam Set<Long> ids) {
        bffService.delete(queryType, ids);


    }

    @GetMapping("{id}")
    public ResponseResult detail(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @PathVariable Long id) {
        BizNewsInfoResponse bizNewsInfoResponse = bffService.detail(queryType, id);
        return super.handleResponseBody(bizNewsInfoResponse);
    }
}
