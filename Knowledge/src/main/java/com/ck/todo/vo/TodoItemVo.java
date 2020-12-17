package com.ck.todo.vo;

import com.ck.todo.po.TodoItemPo;
import lombok.Data;

@Data
public class TodoItemVo extends TodoItemPo {

    private String group;

}
