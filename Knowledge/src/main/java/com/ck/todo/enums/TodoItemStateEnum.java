package com.ck.todo.enums;

import com.ck.common.aop.EnumName;
import com.ck.common.enums.EnumInf;
import lombok.Getter;

@Getter
@EnumName("todo:item:state")
public enum TodoItemStateEnum implements EnumInf {
    WAITING("waiting", "待启动"), RUNNING("running", "进行中"), FINISH("finish", "已完成");

    private String value;

    private String label;

    TodoItemStateEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }
}
