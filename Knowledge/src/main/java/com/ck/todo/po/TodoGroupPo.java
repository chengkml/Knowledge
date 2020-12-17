package com.ck.todo.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "ck_todo_group")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoGroupPo {

    private Long id;

    @Column(length = 64)
    private String name;

    @Column(length = 128)
    private String descr;

    private Long parentId;

    @Column(name = "create_date")
    private Date createDate = new Date();

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public List<TodoGroupPo> children;

    @Transient
    public List<TodoGroupPo> getChildren() {
        return children;
    }
}
