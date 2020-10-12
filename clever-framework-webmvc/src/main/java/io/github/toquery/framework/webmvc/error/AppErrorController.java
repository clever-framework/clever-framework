package io.github.toquery.framework.webmvc.error;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import io.github.toquery.framework.webmvc.domain.ResponseParamBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class AppErrorController extends BasicErrorController {



    public AppErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes, errorProperties);
        log.debug("AppErrorController ErrorAttributes ErrorProperties");
    }

    public AppErrorController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, List<ErrorViewResolver> errorViewResolvers) {
        super(errorAttributes, errorProperties, errorViewResolvers);
        log.debug("AppErrorController ErrorAttributes ErrorProperties errorViewResolvers");
    }

//    public AppErrorController(ServerProperties serverProperties) {
//        super(new DefaultErrorAttributes(serverProperties.getError().isIncludeException()), serverProperties.getError());
//        log.info("初始化 App Web 错误数据返回接口");
//    }


    @Override
    protected Map<String, Object> getErrorAttributes(HttpServletRequest request, ErrorAttributeOptions options) {
        Map<String, Object> superErrorAttributes = super.getErrorAttributes(request, options);
        ResponseParam responseParam = new ResponseParamBuilder().fail().message(superErrorAttributes.getOrDefault("message", "系统错误！").toString()).build();
        return JacksonUtils.object2HashMap(responseParam);
    }

    /*
    @Override
    public Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        Map<String, Object> superErrorAttributes = super.getErrorAttributes(request, includeStackTrace);
        ResponseParam responseParam = new ResponseParamBuilder().message(superErrorAttributes.getOrDefault("message", "系统错误！").toString()).build();
        return JacksonUtils.object2HashMap(responseParam);
    }
    */

}
