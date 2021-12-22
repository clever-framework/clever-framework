package com.toquery.framework.example.bff.admin.news.category.controller;

import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryAddRequest;
import com.toquery.framework.example.bff.admin.news.category.model.request.BizNewsCategoryUpdateRequest;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryInfoResponse;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryListResponse;
import com.toquery.framework.example.bff.admin.news.category.model.response.BizNewsCategoryPageResponse;
import com.toquery.framework.example.bff.admin.news.category.service.BizNewsCategoryService;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsListRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.modules.news.category.entity.BizNewsCategory;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseBFFController;
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
@RequestMapping("/admin/biz-news/category")
public class BizCategoryController extends AppBaseBFFController<BizNewsCategoryService> {

    public static final String MODEL_NAME = "新闻类名管理";

    public static final String BIZ_NAME = "新闻管理";

    public BizCategoryController(BizNewsCategoryService bffService) {
        super(bffService);
    }

    @GetMapping
    public ResponseResult page(BizNewsPageRequest pageRequest) {
        Page<BizNewsCategoryPageResponse> pageResponse = bffService.page(pageRequest);
        return ResponseResult.builder().page(pageResponse).build();
    }

    @GetMapping("/list")
    public ResponseResult list(@RequestParam BizNewsListRequest listRequest) {
        List<BizNewsCategoryListResponse> listResponse = bffService.list(listRequest);
        return ResponseResult.builder().content(listResponse).build();
    }


    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseResult save(@Validated @RequestBody BizNewsCategoryAddRequest addRequest) {
        BizNewsCategoryInfoResponse infoResponse = bffService.save(addRequest);
        return ResponseResult.builder().content(infoResponse).build();

    }

    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseResult update(@RequestBody BizNewsCategoryUpdateRequest updateRequest) {
        BizNewsCategoryInfoResponse infoResponse = bffService.update(updateRequest);
        return ResponseResult.builder().content(infoResponse).build();
    }


    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        bffService.delete(ids);
    }

    @GetMapping("{id}")
    public ResponseResult get(@PathVariable Long id) {
        BizNewsCategoryInfoResponse infoResponse = bffService.get(id);
        return ResponseResult.builder().content(infoResponse).build();
    }
}
