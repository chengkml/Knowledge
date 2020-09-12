package com.ck.knowledge.dao.todo;

import com.ck.knowledge.po.todo.TodoItemPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItemPo, Long>, JpaSpecificationExecutor<TodoItemPo> {
    List<TodoItemPo> findByState(String value);
}
