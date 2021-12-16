package io.github.toquery.framework.files.rest;

import io.github.toquery.framework.core.util.AppDownloadFileUtil;
import io.github.toquery.framework.crud.controller.AppBaseCrudController;
import io.github.toquery.framework.files.constant.AppFileStoreTypeEnum;
import io.github.toquery.framework.files.entity.SysFiles;
import io.github.toquery.framework.files.properties.AppFilesProperties;
import io.github.toquery.framework.files.service.ISysFilesService;
import io.github.toquery.framework.webmvc.annotation.UpperCase;
import io.github.toquery.framework.webmvc.domain.ResponseResult;
import io.github.toquery.framework.webmvc.domain.ResponseBodyBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Map;

/**
 * @author toquery
 * @version 1
 */
@Slf4j
@RestController
public class AppFilesRest extends AppBaseCrudController<ISysFilesService, SysFiles> {

    public AppFilesRest() {
        log.info(this.getClass().getSimpleName());
    }

    @Resource
    private AppFilesProperties appFilesProperties;

    @PostMapping("${app.files.path.upload:/app/files/upload}")
    public ResponseResult uploadFile(@RequestParam(value = "fileStoreType", defaultValue = "DATABASE", required = false) @UpperCase AppFileStoreTypeEnum fileStoreType, MultipartRequest multipartRequest) throws IOException {
        ResponseBodyBuilder responseParam = ResponseResult.builder();
        if (fileStoreType == AppFileStoreTypeEnum.DATABASE) {
            SysFiles sysFiles = doaminService.saveFiles(multipartRequest.getFile(appFilesProperties.getUploadParam()));
            sysFiles.setFullDownloadPath(this.formatDownloadPath(sysFiles));
            responseParam.content(sysFiles);
        } else if (fileStoreType == AppFileStoreTypeEnum.DB) {
            SysFiles sysFiles = doaminService.saveFiles(multipartRequest.getFile(appFilesProperties.getUploadParam()));
            sysFiles.setFullDownloadPath(this.formatDownloadPath(sysFiles));
            responseParam.content(sysFiles);
        } else if (fileStoreType == AppFileStoreTypeEnum.FILE) {
            String filePath = doaminService.storeFiles(multipartRequest.getFile(appFilesProperties.getUploadParam()));
            SysFiles sysFiles = new SysFiles();
            sysFiles.setFullDownloadPath(filePath);
            responseParam.content(sysFiles);
        } else {
            responseParam.message("上传文件错误");
        }
        return responseParam.build();
    }

    @RequestMapping("/app/files/download/{id}.{extension}")
    public ResponseEntity downloadFile(@PathVariable("id") Long id, @PathVariable String extension) {
        ResponseEntity responseEntity = null;
        try {
            SysFiles sysFiles = doaminService.getByIdAndExtension(id, extension);
            responseEntity = AppDownloadFileUtil.download(appFilesProperties.getPath().getStore() + sysFiles.getStoragePath(), sysFiles.getStorageName(), sysFiles.getOriginName(), sysFiles.getMimeType());
        } catch (Exception e) {
            e.printStackTrace();
            responseEntity = super.notFound("文件未找到");
        }
        return responseEntity;
    }

    @RequestMapping("${app.files.path.download:/app/files/download}")
    public ResponseEntity downloadFile(@RequestParam("id") Long id) {
        ResponseEntity responseEntity = null;
        try {
            SysFiles sysFiles = super.getById(id);
            responseEntity = AppDownloadFileUtil.download(appFilesProperties.getPath().getStore() + sysFiles.getStoragePath(), sysFiles.getStorageName(), sysFiles.getOriginName(),sysFiles.getMimeType());
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
        String result = appFilesProperties.getPath().getDownload();
        try {
            // result.replace("{id}", sysFiles.getId().toString()).replace("{extension}", sysFiles.getExtension());
            Map<String, Object> uriVariables = PropertyUtils.describe(sysFiles);
            result = UriComponentsBuilder.fromUriString(appFilesProperties.getPath().getDownload()).uriVariables(uriVariables).build().toUriString();
        } catch (Exception e) {
            log.error("处理文件下载路径出错，文件ID {} ，文件存储路径 {} , 文件存储名称 {} ", sysFiles.getId(), sysFiles.getStoragePath(), sysFiles.getStorageName());
            e.printStackTrace();
        }
        return result;

    }


}
