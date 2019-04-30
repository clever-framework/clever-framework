package io.github.toquery.framework.data.rest;

import io.github.toquery.framework.web.domain.ResponsePage;
import io.github.toquery.framework.web.domain.ResponsePageBuilder;
import io.github.toquery.framework.web.domain.ResponseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.mvc.ResourceProcessorHandlerMethodReturnValueHandler;
import org.springframework.hateoas.mvc.ResourceProcessorInvoker;
import org.springframework.http.HttpEntity;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
public class AppHandlerMethodReturnValueHandler  implements HandlerMethodReturnValueHandler {


    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return true;
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter methodParameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
//        if (PagedResources.class.isInstance(returnValue)) {
//            PagedResources pagedResources = (PagedResources) returnValue;
//            returnValue = ResponseParam.success().page(pagedResources);
//            log.info("");
//        }

    }
}
