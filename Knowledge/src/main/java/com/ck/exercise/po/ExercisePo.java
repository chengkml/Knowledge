package com.ck.exercise.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @Title: ExercisePo
 * @Description: 练习表
 * @Author: Chengkai
 * @Date: 2019/8/29 13:03
 * @Version: 1.0
 */
@Entity(name="ck_exercise")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExercisePo {

    private Long id;

    private String code;

    @Column(length=512)
    private String categorys;

    private Date createDate = new Date();

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }

    private List<QuestionPo> questions;

    @Transient
    public List<QuestionPo> getQuestions(){
        return this.questions;
    }
}
