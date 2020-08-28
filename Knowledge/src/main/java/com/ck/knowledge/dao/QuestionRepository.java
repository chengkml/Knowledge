package com.ck.knowledge.dao;

import com.ck.knowledge.po.QuestionPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface QuestionRepository extends JpaRepository<QuestionPo, Long>, JpaSpecificationExecutor<QuestionPo> {

    @Transactional
    @Modifying(clearAutomatically = true)
    void deleteByKnowledgeId(Long knowledgeId);

    List<QuestionPo> findByKnowledgeId(@Param("knowledgeId") Long knowledgeId);

    @Query(value = "select * from ck_question q where q.category in ?1 order by RAND() limit ?2", nativeQuery=true)
    List<QuestionPo> randQuestionByCategory(List<Long> categorys, Integer num);

    @Query(value = "select * from ck_question q order by RAND() limit ?1", nativeQuery=true)
    List<QuestionPo> randQuestion(Integer num);
}
