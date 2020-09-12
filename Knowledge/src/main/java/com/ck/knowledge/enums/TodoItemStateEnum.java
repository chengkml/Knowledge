package com.ck.knowledge.enums;

import com.ck.knowledge.aop.EnumName;
import lombok.Getter;

@Getter
@EnumName("todo:item:state")
public enum TodoItemStateEnum implements EnumInf{
    WAITING("waiting","待启动"),RUNNING("running","进行中"),FINISH("finish","已完成");

    private String value;

    private String label;

    private TodoItemStateEnum(String value, String label){
        this.value = value;
        this.label = label;
    }
}
