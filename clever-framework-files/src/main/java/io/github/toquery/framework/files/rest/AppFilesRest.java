package io.github.toquery.framework.files.rest;

import io.github.toquery.framework.common.util.AppDownloadFileUtil;
import io.github.toquery.framework.curd.controller.AppBaseCurdController;
import io.github.toquery.framework.files.domain.SysFiles;
import io.github.toquery.framework.files.properties.AppFilesProperties;
import io.github.toquery.framework.files.service.ISysFilesService;
import io.github.toquery.framework.webmvc.domain.ResponseParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@RestController
public class AppFilesRest extends AppBaseCurdController<ISysFilesService, SysFiles, Long> {

    @Resource
    private AppFilesProperties appFilesProperties;

    @PostMapping("${app.files.path.upload:/app/files/upload}")
    public ResponseParam uploadFile(MultipartRequest multipartRequest) throws IOException {
        SysFiles sysFiles = service.saveFiles(multipartRequest.getFile(appFilesProperties.getUploadParam()));
        sysFiles.setFullDownloadPath(this.formatDownloadPath(sysFiles));
        return super.handleResponseParam(sysFiles);
    }
    @RequestMapping("${app.files.path.download:/app/files/download}")
    public ResponseEntity downloadFile(@RequestParam("id") Long id) {
        ResponseEntity responseEntity = null;
        try {
            SysFiles sysFiles = super.getById(id);
            responseEntity = AppDownloadFileUtil.download( appFilesProperties.getPath().getStore() + sysFiles.getStoragePath(), sysFiles.getStorageName(), sysFiles.getOriginName());
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = super.notFound("文件未找到");
        }
        return responseEntity;
    }

    /**
     * 格式化下载路径
     *
     * @param sysFiles 文件
     * @return 下载路径
     */
    private String formatDownloadPath(SysFiles sysFiles) {
        String result = appFilesProperties.getPath().getDownload() + "?id=" + sysFiles.getId();
       /* try {
            Map<String, Object> uriVariables = PropertyUtils.describe(sysFiles);
            result = UriComponentsBuilder.fromUriString(appFilesProperties.getPath().getDownload()).uriVariables(uriVariables).build().toUriString();
        } catch (Exception e) {
            log.error("处理文件下载路径出错，文件ID {} ，文件存储路径 {} , 文件存储名称 {} ", sysFiles.getId(), sysFiles.getStoragePath(), sysFiles.getStorageName());
            e.printStackTrace();
        }*/
        return result;

    }


}
