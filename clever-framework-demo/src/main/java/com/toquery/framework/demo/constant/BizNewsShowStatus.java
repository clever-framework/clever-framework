package com.toquery.framework.demo.constant;

import io.github.toquery.framework.web.dict.AppDictRuntime;
import lombok.Getter;

@Getter
public enum BizNewsShowStatus implements AppDictRuntime {
    DRAFT("草稿"), SHOW("显示"), HIDE("隐藏");

    private final String remark;

    BizNewsShowStatus(String remark) {
        this.remark = remark;
    }
}
