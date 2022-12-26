package io.github.toquery.framework.files.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toquery.framework.core.entity.AppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * 附件持久化存储对象
 */

@Data
@EqualsAndHashCode(callSuper=false)
@Entity
@TableName(value = "")
@Table(name = "sys_files")
public class SysFiles extends AppBaseEntity {

    /**
     * 存储名称
     */
    @JsonIgnore
    @TableField(value = "")
    @Column(name = "storage_name")
    private String storageName;

    @TableField(value = "")
    @Column(name = "mime_type")
    private String mimeType;

    /**
     * 存储路径
     */
    @JsonIgnore
    @TableField(value = "")
    @Column(name = "storage_path")
    private String storagePath;

    /**
     * 大小
     */
    @TableField(value = "")
    @Column(name = "size")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long size;

    /**
     * 文件后缀
     */
    @TableField(value = "")
    @Column(name = "extension")
    private String extension;


    /**
     * 原名称
     */
    @TableField(value = "")
    @Column(name = "origin_name")
    private String originName;

    /**
     * 排序号
     */
    @TableField(value = "")
    @Column(name = "sort_number")
    private int sortNumber;


    /**
     * 业务id
     */
    @TableField(value = "")
    @Column(name = "business_id")
    private String businessId;

    /**
     * 业务属性名称
     */
    @TableField(value = "")
    @Column(name = "business_name")
    private String businessName;

    @Transient
    private String fullDownloadPath;

}

