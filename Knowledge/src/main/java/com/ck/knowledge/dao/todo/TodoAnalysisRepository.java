package com.ck.knowledge.dao.todo;

import com.ck.knowledge.po.todo.TodoAnalysisPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TodoAnalysisRepository extends JpaRepository<TodoAnalysisPo, Long>, JpaSpecificationExecutor<TodoAnalysisPo> {
    List<TodoAnalysisPo> findByTodoId(long id);
}
