package com.toquery.framework.example.bff.api.news.info.server;

import com.toquery.framework.example.bff.api.news.info.model.request.BizNewsPageRequest;
import com.toquery.framework.example.bff.api.news.info.service.BizNewsGRpcService;
import io.github.toquery.framework.grpc.core.annotation.GRpcMethod;
import io.github.toquery.framework.grpc.server.annotation.GRpcServer;
import io.github.toquery.framework.web.domain.ResponseBodyWrap;
import io.grpc.stub.StreamObserver;

import javax.annotation.Resource;

/**
 *
 */
@GRpcServer("/news")
public class BizNewsGRpcServer {

    @Resource
    private BizNewsGRpcService bizNewsGRpcService;


    /**
     * App版本分页列表
     *
     * @param request  App版本分页请求
     * @param response 响应
     */
    @GRpcMethod("/page")
    public void page(BizNewsPageRequest request, StreamObserver<ResponseBodyWrap<?>> response) {
        response.onNext(ResponseBodyWrap.builder().success().content(bizNewsGRpcService.page(request)).build());
        response.onCompleted();
    }
}
