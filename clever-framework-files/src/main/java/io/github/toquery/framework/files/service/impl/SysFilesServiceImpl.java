package io.github.toquery.framework.files.service.impl;

import com.google.common.io.Files;
import io.github.toquery.framework.common.util.AppDateUtil;
import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.crud.service.impl.AppBaseServiceImpl;
import io.github.toquery.framework.files.domain.SysFiles;
import io.github.toquery.framework.files.properties.AppFilesProperties;
import io.github.toquery.framework.files.repository.SysFilesRepository;
import io.github.toquery.framework.files.service.ISysFilesService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author toquery
 * @version 1
 */
@Service
public class SysFilesServiceImpl extends AppBaseServiceImpl<Long, SysFiles, SysFilesRepository> implements ISysFilesService {
    /**
     * 查询条件表达式
     */
    private Map<String, String> expressionMap = new LinkedHashMap<String, String>() {
        {
            put("id", "id:EQ");
            put("extension", "extension:EQ");
            put("extensionIn", "extension:IN");
        }
    };

    @Resource
    private AppFilesProperties appFilesProperties;

    @Override
    public Map<String, String> getQueryExpressions() {
        return expressionMap;
    }

    @Override
    public SysFiles saveFiles(MultipartFile file) throws IOException {
        //文件原名
        String originalFilename = file.getOriginalFilename();
        // 文件扩展名
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        //文件存储路径
        String storeWithDate = appFilesProperties.getPath().getStoreWithDate();
        //新文件名称
        String newFileName = UUID.randomUUID().toString() + "." + fileExtension;
        // 新文件存储路径
        File newFile = new File(storeWithDate + newFileName);
        //保存文件
        FileUtils.copyToFile(file.getInputStream(), newFile);

        SysFiles sysFiles = new SysFiles();
        sysFiles.setOriginName(originalFilename);
        sysFiles.setSize(file.getSize());
        sysFiles.setStorageName(newFileName);
        sysFiles.setExtension(fileExtension);
        sysFiles.setStoragePath(AppDateUtil.getCurrentDate());
        sysFiles.setMimeType(file.getContentType());
        return super.save(sysFiles);
    }

    @Override
    public String storeFiles(MultipartFile file) throws IOException {
        //文件原名
        String originalFilename = file.getOriginalFilename();
        //文件存储路径
        String storeWithDate = appFilesProperties.getPath().getStoreWithDate();
        // 文件扩展名
        String fileExtension = Files.getFileExtension(originalFilename);
        //新文件路径名称
        String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

        // 新文件存储路径
        File newFile = new File(storeWithDate + newFileName);
        //保存文件
        FileUtils.copyToFile(file.getInputStream(), newFile);
        return appFilesProperties.getShowDomain() + File.separator + AppDateUtil.getCurrentDate() + File.separator + newFileName;
    }

    @Override
    public SysFiles getByIdAndExtension(Long id, String extension) throws AppException {
        Map<String, Object> map = new HashMap<>();
        List<SysFiles> sysFilesList = this.find(map);
        if (sysFilesList == null || sysFilesList.isEmpty()) {
            throw new AppException("未找到 " + id + "." + extension + " 文件");
        }
        return sysFilesList.get(0);
    }

}
