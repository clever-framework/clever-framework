package com.toquery.framework.example.sdk.news.info;

import com.toquery.framework.example.sdk.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.sdk.news.info.model.response.BizNewsPageResponse;
import io.github.toquery.framework.grpc.client.annotation.GRpcClient;
import io.github.toquery.framework.grpc.core.annotation.GRpcMethod;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;

import java.util.List;

/**
 *
 */
@GRpcClient(value = "clever-framework-example", scheme = "/news")
public interface BizNewInfoClient {

    /**
     * 分页查询
     */
    @GRpcMethod("/page")
    ResponseBodyWrap<List<BizNewsPageResponse>> page(BizNewsPageRequest request);

}
