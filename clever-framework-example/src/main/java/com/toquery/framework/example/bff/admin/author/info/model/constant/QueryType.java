package com.toquery.framework.example.bff.admin.author.info.model.constant;

import lombok.Getter;

@Getter
public enum QueryType {

    FILTER("hibernate_filter"),
    APP("框架封装"),
    JPA("jpa"),
    JPA_ANNOTATION("jpa注解"),
    MYBATIS("mybatis");

    private final String remark;

    QueryType(String remark) {
        this.remark = remark;
    }
}
