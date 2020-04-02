package com.ck.knowledge.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Title: ExerciseResultPo
 * @Description: 练习结果表
 * @Author: Chengkai
 * @Date: 2019/8/29 13:04
 * @Version: 1.0
 */
@Entity(name="ck_exercise_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseResultPo {

    private Long id;

    private Long exerciseId;

    private Long questionId;

    @Column(length=32)
    private String answer;

    @Column(length=8)
    private String isCorrect;

    private Date createDate = new Date();

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }
}
