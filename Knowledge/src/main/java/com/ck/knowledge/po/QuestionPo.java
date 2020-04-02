package com.ck.knowledge.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Title: QuestionPo
 * @Description: 问题表
 * @Author: Chengkai
 * @Date: 2019/8/29 13:04
 * @Version: 1.0
 */
@Entity
@Table(name="ck_question")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionPo {

    private Long id;

    @Column(length=8)
    private String type;

    @Column(length=128)
    private String stem;

    @Column(length=512)
    private String options;

    @Column(length=32)
    private String result;

    private Long knowledgeId;

    private Date createDate = new Date();

    private Long category;

    private List<String> optionItems;

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

    @Transient
    public List<String> getOptionItems(){
        return this.optionItems;
    }
}
