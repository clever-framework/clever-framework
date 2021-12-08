package com.toquery.framework.example.modules.news.info.constant;

import io.github.toquery.framework.web.dict.AppDictRuntime;
import lombok.Getter;

@Getter
//@AppDict("show_status")
public enum BizNewsShowStatus implements AppDictRuntime {

    DRAFT("草稿"), SHOW("显示"), HIDE("隐藏");

    private String remark;

    BizNewsShowStatus(){
    }

    BizNewsShowStatus(String remark) {
        this.remark = remark;
    }
}
