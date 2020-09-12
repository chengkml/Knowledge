package com.ck.knowledge.po.todo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "ck_todo_analysis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoAnalysisPo {

    private Long id;

    @Column(length = 128)
    private String descr;

    @Column(name = "todo_id")
    private Long todoId;

    @Column(name = "create_date")
    private Date createDate = new Date();

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }
}
