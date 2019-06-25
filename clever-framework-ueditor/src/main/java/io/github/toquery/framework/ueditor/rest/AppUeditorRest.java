package io.github.toquery.framework.ueditor.rest;

import com.google.common.base.Strings;
import io.github.toquery.framework.common.util.DownloadFileUtil;
import io.github.toquery.framework.ueditor.UeditorConfigManager;
import io.github.toquery.framework.ueditor.define.ActionMap;
import io.github.toquery.framework.ueditor.define.AppInfo;
import io.github.toquery.framework.ueditor.define.BaseState;
import io.github.toquery.framework.ueditor.define.State;
import io.github.toquery.framework.ueditor.hunter.FileManager;
import io.github.toquery.framework.ueditor.hunter.ImageHunter;
import io.github.toquery.framework.ueditor.properties.AppUeditorProperties;
import io.github.toquery.framework.ueditor.upload.Uploader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@RestController
@RequestMapping("/app")
public class AppUeditorRest {

    @Resource
    private HttpServletRequest request;

    @Resource
    private UeditorConfigManager ueditorConfigManager;

    @Resource
    private AppUeditorProperties appUeditorProperties;


    /**
     * ueditor 编辑器获取配置接口
     *
     */
    @RequestMapping("/ueditor")
    public String resquest(@RequestParam(value = "action") String action, @RequestParam(value = "callback", required = false) String callbackName) throws IOException, ServletException {

        // 验证提交参数,配置错误
        if (!ActionMap.mapping.containsKey(action)) {
            return (new BaseState(false, AppInfo.INVALID_ACTION)).toJSONString();
        }

        // 配置无效
        if (!ueditorConfigManager.valid()) {
            return (new BaseState(false, AppInfo.CONFIG_ERROR)).toJSONString();
        }

        if (Strings.isNullOrEmpty(callbackName)) {
            return this.invoke(action);
        } else {
            return !this.validCallbackName(callbackName) ? (new BaseState(false, 401)).toJSONString() : callbackName + "(" + this.invoke(action) + ");";
        }
    }


    /**
     * 显示富文本编辑上传的内容
     * 正常请求路径：/app/ueditor/view/image/2019-06-24/1561374380813048514.png
     *
     */
    @GetMapping("/files/ueditor/{type}/{date}/{fileName}")
    public ResponseEntity viewFile(@PathVariable String type, @PathVariable String date, @PathVariable String fileName) {
        // 文件存储的全路径
        String fullFilePath = appUeditorProperties.getStorePath() + File.separator + type + File.separator + date + File.separator + fileName;
        return DownloadFileUtil.download(fullFilePath, fileName);
    }

    public String invoke(String action) throws IOException, ServletException {
        State state = null;
        int actionCode = ActionMap.getType(action);
        Map<String, Object> conf = null;
        switch (actionCode) {
            case ActionMap.CONFIG:
                return this.ueditorConfigManager.getAllConfig().toString();
            case ActionMap.UPLOAD_IMAGE:
            case ActionMap.UPLOAD_SCRAWL:
            case ActionMap.UPLOAD_VIDEO:
            case ActionMap.UPLOAD_FILE:
                conf = this.ueditorConfigManager.getConfig(actionCode);
                state = (new Uploader(this.request, appUeditorProperties.getUploadParam(), appUeditorProperties.getStorePath(), conf)).doExec();
                break;
            case ActionMap.CATCH_IMAGE:
                conf = this.ueditorConfigManager.getConfig(actionCode);
                String[] list = this.request.getParameterValues((String) conf.get("fieldName"));
                state = (new ImageHunter(conf)).capture(list);
                break;
            case ActionMap.LIST_FILE:
            case ActionMap.LIST_IMAGE:
                conf = this.ueditorConfigManager.getConfig(actionCode);
                int start = this.getStartIndex();
                state = (new FileManager(conf)).listFile(start);
        }

        return state.toJSONString();

    }

    public int getStartIndex() {
        String start = this.request.getParameter("start");
        try {
            return Integer.parseInt(start);
        } catch (Exception var3) {
            return 0;
        }
    }

    public boolean validCallbackName(String name) {
        return name.matches("^[a-zA-Z_]+[\\w0-9_]*$");
    }
}
