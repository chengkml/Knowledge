package com.ck.knowledge.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Title: KnowledgePo
 * @Description: 知识点表
 * @Author: Chengkai
 * @Date: 2019/6/19 21:03
 * @Version: 1.0
 */
@Entity(name="ck_knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgePo {

    private Long id;

    @Column(length=64)
    private String name;

    private Long category;

    @Column(length=512)
    private String descr;

    private Date createDate = new Date();

    @Lob
    private String detail;

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

    public List<QuestionPo> questions;

    @Transient
    public List<QuestionPo> getQuestions(){
        return this.questions;
    }
}
