package com.ck.knowledge.enums;

import com.ck.knowledge.aop.EnumName;
import lombok.Getter;

@Getter
@EnumName("res:valid")
public enum ResValidEnum implements EnumInf {
    VALID("valid", "生效"), INVALID("invalid", "未生效");

    private String value;

    private String label;

    ResValidEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
