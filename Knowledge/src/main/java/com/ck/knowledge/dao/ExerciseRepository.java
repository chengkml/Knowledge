package com.ck.knowledge.dao;

import com.ck.knowledge.po.ExercisePo;
import com.ck.knowledge.po.QuestionPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Title: ExerciseRepository
 * @Author: Chengkai
 * @Date: 2019/9/15 19:54
 * @Version: 1.0
 */
public interface ExerciseRepository extends JpaRepository<ExercisePo, Long>, JpaSpecificationExecutor<ExercisePo> {

}
