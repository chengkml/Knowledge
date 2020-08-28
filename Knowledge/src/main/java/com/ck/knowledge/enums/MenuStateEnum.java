package com.ck.knowledge.enums;

import com.ck.knowledge.aop.EnumName;
import lombok.Getter;

@Getter
@EnumName("menu:state")
public enum MenuStateEnum implements EnumInf{
    VALID("valid","生效"),INVALID("invalid","失效");

    private String value;

    private String label;

    private MenuStateEnum(String value, String label){
        this.value = value;
        this.label = label;
    }
}
