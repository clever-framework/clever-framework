package io.github.toquery.framework.files.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toquery.framework.dao.entity.AppBaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 附件持久化存储对象
 */

@Data
@Entity
@Table(name = "sys_files")
public class SysFiles extends AppBaseEntity {

    /**
     * 存储名称
     */
    @JsonIgnore
    @Column(name = "storage_name")
    private String storageName;

    @Column(name = "mime_type")
    private String mimeType;

    /**
     * 存储路径
     */
    @JsonIgnore
    @Column(name = "storage_path")
    private String storagePath;

    /**
     * 大小
     */
    @Column(name = "size")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long size;

    /**
     * 文件后缀
     */
    @Column(name = "extension")
    private String extension;


    /**
     * 原名称
     */
    @Column(name = "origin_name")
    private String originName;

    /**
     * 排序号
     */
    @Column(name = "sort_number")
    private int sortNumber;


    /**
     * 业务id
     */
    @Column(name = "business_id")
    private String businessId;

    /**
     * 业务属性名称
     */
    @Column(name = "business_name")
    private String businessName;

    @Transient
    private String fullDownloadPath;

}

