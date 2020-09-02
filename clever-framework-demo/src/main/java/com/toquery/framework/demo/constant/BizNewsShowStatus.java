package com.toquery.framework.demo.constant;

import lombok.Getter;

@Getter
public enum BizNewsShowStatus {
    DRAFT("草稿"), SHOW("显示"), HIDE("隐藏");

    private final String remark;

    BizNewsShowStatus(String remark) {
        this.remark = remark;
    }
}
