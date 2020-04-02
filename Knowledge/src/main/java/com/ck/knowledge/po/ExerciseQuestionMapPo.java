package com.ck.knowledge.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Title: ExerciseQuestionMapPo
 * @Description: 练习与题目映射表
 * @Author: Chengkai
 * @Date: 2019/8/29 13:11
 * @Version: 1.0
 */
@Entity(name="ck_exercise_question_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseQuestionMapPo {

    private Long id;

    private Long exerciseId;

    private Long questionId;

    @Id
    @GeneratedValue
    public Long getId(){
        return id;
    }
}
