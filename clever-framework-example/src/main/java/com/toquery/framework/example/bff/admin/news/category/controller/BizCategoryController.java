package com.toquery.framework.example.bff.admin.news.category.controller;

import com.toquery.framework.example.bff.admin.news.category.dao.BizNewsCategoryDao;
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
import io.github.toquery.framework.web.domain.ResponseBody;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
public class BizCategoryController extends AppBaseBFFController<BizNewsCategory, BizNewsCategoryService, BizNewsCategoryDao> {

    public static final String MODEL_NAME = "新闻管理";

    public static final String BIZ_NAME = "新闻类目管理";

    public BizCategoryController(BizNewsCategoryService bffService) {
        super(bffService);
    }

    @Operation(summary = "分页查询新闻栏目", description = "分页查询新闻栏目", tags = { "biz" })
    @ApiResponse(description = "分页查询新闻栏目",
            content = {
                    @Content(mediaType = "application/json",
                            array =
                            @ArraySchema(schema = @Schema(implementation = BizNewsCategoryPageResponse.class))
                    )
            }
    )
    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping
    public ResponseBody page(BizNewsPageRequest pageRequest) {
        Page<BizNewsCategoryPageResponse> pageResponse = super.bffService.page(pageRequest);
        return ResponseBody.builder().page(pageResponse).build();
    }

    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping("/list")
    public ResponseBody list(@RequestParam BizNewsListRequest listRequest) {
        List<BizNewsCategoryListResponse> listResponse = super.bffService.list(listRequest);
        return ResponseBody.builder().content(listResponse).build();
    }


    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.CREATE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PostMapping
    public ResponseBody save(@Validated @RequestBody BizNewsCategoryAddRequest addRequest) {
        BizNewsCategoryInfoResponse infoResponse = super.bffService.save(addRequest);
        return ResponseBody.builder().content(infoResponse).build();

    }

    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.MODIFY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @PutMapping
    public ResponseBody update(@RequestBody BizNewsCategoryUpdateRequest updateRequest) {
        BizNewsCategoryInfoResponse infoResponse = super.bffService.update(updateRequest);
        return ResponseBody.builder().content(infoResponse).build();
    }


    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.DELETE, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @DeleteMapping
    public void delete(@RequestParam Set<Long> ids) {
        super.bffService.delete(ids);
    }

    @AppLogMethod(value = BizNewsCategory.class, logType = AppLogType.QUERY, modelName = MODEL_NAME, bizName = BIZ_NAME)
    @GetMapping("{id}")
    public ResponseBody get(@PathVariable Long id) {
        BizNewsCategoryInfoResponse infoResponse = super.bffService.get(id);
        return ResponseBody.builder().content(infoResponse).build();
    }
}
