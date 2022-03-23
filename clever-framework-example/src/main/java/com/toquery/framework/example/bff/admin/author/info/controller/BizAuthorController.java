package com.toquery.framework.example.bff.admin.author.info.controller;

import com.toquery.framework.example.bff.admin.author.info.dao.BizAuthorDao;
import com.toquery.framework.example.bff.admin.author.info.model.constant.QueryType;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorAddRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorListRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorPageRequest;
import com.toquery.framework.example.bff.admin.author.info.model.request.BizAuthorUpdateRequest;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorInfoResponse;
import com.toquery.framework.example.bff.admin.author.info.model.response.BizAuthorListResponse;
import com.toquery.framework.example.bff.admin.author.info.service.BizAuthorService;
import com.toquery.framework.example.modules.author.info.entity.BizAuthor;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseBFFController;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
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
@RequestMapping("/admin/biz-author")
public class BizAuthorController extends AppBaseBFFController<BizAuthor, BizAuthorService, BizAuthorDao> {

    public static final String MODEL_NAME = "作者管理";

    public static final String BIZ_NAME = "作者管理";

    public BizAuthorController(BizAuthorService bffService) {
        super(bffService);
    }

    @GetMapping
    public ResponseBodyWrap<?> page(BizAuthorPageRequest bizAuthorPageRequest) {
        Page<BizAuthor> bizAuthorPage = bffService.page(bizAuthorPageRequest);
        return ResponseBodyWrap.builder().page(bizAuthorPage).build();
    }

    @GetMapping("/multi")
    public ResponseBodyWrap<?> pageMultiType(BizAuthorPageRequest bizAuthorPageRequest) {
        return bffService.pageMultiType(bizAuthorPageRequest);
    }

    @GetMapping("/list")
    public ResponseBodyWrap<?> list(BizAuthorListRequest bizAuthorListRequest) {
        List<BizAuthorListResponse> listResponses = bffService.list(bizAuthorListRequest);
        return super.handleResponseBody(listResponses);
    }


    @AppLogMethod(value = BizAuthor.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseBodyWrap<?> save(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @Validated @RequestBody BizAuthorAddRequest request) {
        BizAuthorInfoResponse bizAuthorInfoResponse = bffService.save(queryType, request);
        return super.handleResponseBody(bizAuthorInfoResponse);
    }

    @AppLogMethod(value = BizAuthor.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseBodyWrap<?> update(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestBody BizAuthorUpdateRequest request) {

        BizAuthorInfoResponse bizAuthorInfoResponse = bffService.update(queryType, request);
        return super.handleResponseBody(bizAuthorInfoResponse);
    }


    @AppLogMethod(value = BizAuthor.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void delete(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestParam Set<Long> ids) {
        bffService.delete(queryType, ids);
    }

    @GetMapping("{id}")
    public ResponseBodyWrap<?> detail(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @PathVariable Long id) {
        BizAuthorInfoResponse bizAuthorInfoResponse = bffService.detail(queryType, id);
        return super.handleResponseBody(bizAuthorInfoResponse);
    }
}
