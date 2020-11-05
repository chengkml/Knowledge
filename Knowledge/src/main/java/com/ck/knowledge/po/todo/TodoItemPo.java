package com.ck.knowledge.po.todo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "ck_todo_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemPo {

    private Long id;

    @Column(length = 128)
    private String name;

    @Column(length = 1024)
    private String analysis;

    @Column(name = "group_id")
    private Long groupId;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Column(name = "lastupd_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date lastUpdDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "estimate_start_time")
    private Date estimateStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "estimate_end_time")
    private Date estimateEndTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "lead_time")
    private Date leadTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "finish_time")
    private Date finishTime;

    @Column(length = 8)
    private String state;

    private Long leftTime;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    @Transient
    public Long getLeftTime() {
        return this.leadTime.getTime() - new Date().getTime();
    }
}
