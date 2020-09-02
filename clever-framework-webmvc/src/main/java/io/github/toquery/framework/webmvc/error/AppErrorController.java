package io.github.toquery.framework.webmvc.error;

import io.github.toquery.framework.common.util.JacksonUtils;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
public class AppErrorController extends BasicErrorController {

    public AppErrorController(ServerProperties serverProperties) {
        super(new DefaultErrorAttributes(serverProperties.getError().isIncludeException()), serverProperties.getError());
        log.info("初始化 App Web 错误数据返回接口");
    }

    @Override
    public Map<String, Object> getErrorAttributes(HttpServletRequest request, boolean includeStackTrace) {
        Map<String, Object> superErrorAttributes = super.getErrorAttributes(request, includeStackTrace);
        ResponseParam responseParam = ResponseParam.builder().build().message(superErrorAttributes.getOrDefault("message", "系统错误！").toString());
        return JacksonUtils.object2HashMap(responseParam);
    }

}
