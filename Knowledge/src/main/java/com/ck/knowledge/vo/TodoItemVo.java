package com.ck.knowledge.vo;

import com.ck.knowledge.po.todo.TodoItemPo;
import lombok.Data;

@Data
public class TodoItemVo extends TodoItemPo {

    private String group;

}
