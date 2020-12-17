package com.ck.exercise.dao;

import com.ck.exercise.po.ExercisePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @Title: ExerciseRepository
 * @Author: Chengkai
 * @Date: 2019/9/15 19:54
 * @Version: 1.0
 */
public interface ExerciseRepository extends JpaRepository<ExercisePo, Long>, JpaSpecificationExecutor<ExercisePo> {

}
