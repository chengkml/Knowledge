package com.ck.menu.enums;

import com.ck.common.aop.EnumName;
import com.ck.common.enums.EnumInf;
import lombok.Getter;

@Getter
@EnumName("menu:state")
public enum MenuStateEnum implements EnumInf {
    VALID("valid", "生效"), INVALID("invalid", "失效");

    private String value;

    private String label;

    MenuStateEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
