package com.ck.knowledge.dao;

import com.ck.knowledge.po.ExerciseQuestionMapPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Title: ExerciseQuestionMapRepository
 * @Author: Chengkai
 * @Date: 2019/9/19 13:10
 * @Version: 1.0
 */
public interface ExerciseQuestionMapRepository extends JpaRepository<ExerciseQuestionMapPo, Long>, JpaSpecificationExecutor<ExerciseQuestionMapPo> {
}
