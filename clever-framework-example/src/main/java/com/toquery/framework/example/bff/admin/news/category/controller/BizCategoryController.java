package com.toquery.framework.example.bff.admin.news.category.controller;

import com.toquery.framework.example.bff.admin.news.category.service.BizNewsCategoryService;
import com.toquery.framework.example.bff.admin.news.info.model.constant.QueryType;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsListRequest;
import com.toquery.framework.example.bff.admin.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.modules.news.category.entity.BizNewsCategory;
import io.github.toquery.framework.core.log.AppLogType;
import io.github.toquery.framework.core.log.annotation.AppLogMethod;
import io.github.toquery.framework.crud.controller.AppBaseBFFController;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
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

    public static final String MODEL_NAME = "新闻管理";

    public static final String BIZ_NAME = "新闻管理";

    public BizCategoryController(BizNewsCategoryService bffService) {
        super(bffService);
    }

    @GetMapping
    public ResponseResult page(BizNewsPageRequest bizNewsPageRequest) {
        return bffService.page(bizNewsPageRequest);
    }

    @GetMapping("/list")
    public ResponseResult list(@RequestParam BizNewsListRequest bizNewsListRequest) {
//        return super.list();
        return null;
    }


    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseResult save(@Validated @RequestBody BizNewsCategory bizNews) {
//        return super.handleResponseParam(super.bffService.save(bizNews));
        return null;
    }

    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseResult update(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestBody BizNewsCategory bizNewsCategory) {
//        ResponseParam responseParam = super.update(bizNewsCategory);
//        return responseParam;
        return null;
    }


    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void delete(@UpperCase @RequestParam(defaultValue = "APP") QueryType queryType, @RequestParam Set<Long> ids) {
//        super.delete(ids);
    }

    @GetMapping("{id}")
    public ResponseResult detail(@RequestParam(defaultValue = "APP") @UpperCase QueryType queryType, @PathVariable Long id) {
//        ResponseParam responseParam = super.detail(id);
//        return responseParam;
        return null;
    }
}
