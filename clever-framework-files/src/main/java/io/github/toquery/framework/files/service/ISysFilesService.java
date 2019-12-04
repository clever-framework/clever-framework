package io.github.toquery.framework.files.service;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.files.domain.SysFiles;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;

/**
 * @author toquery
 * @version 1
 */
public interface ISysFilesService extends AppBaseService<SysFiles, Long> {
    SysFiles saveFiles(MultipartFile file) throws IOException;

    String storeFiles(MultipartFile multipartRequest) throws IOException;

    SysFiles getByIdAndExtension(Long id, String extension) throws AppException;

}
