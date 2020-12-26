package com.ck.job.enums;

import com.ck.common.aop.EnumName;
import com.ck.common.enums.EnumInf;
import lombok.Getter;

@Getter
@EnumName("job:type")
public enum JobTypeEnum implements EnumInf {
    CRON("cron", "定时任务"), DEPEND("depend", "依赖任务");

    private String value;

    private String label;

    JobTypeEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
