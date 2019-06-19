package io.github.toquery.framework.files.service;

import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.files.domain.SysFiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author toquery
 * @version 1
 */
public interface ISysFilesService extends AppBaseService<SysFiles, Long> {
    SysFiles saveFiles(MultipartFile file) throws IOException;
}
