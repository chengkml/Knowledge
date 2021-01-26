package com.ck.todo.dao;

import com.ck.todo.po.TodoItemPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;
import java.util.List;

public interface TodoItemRepository extends JpaRepository<TodoItemPo, Long>, JpaSpecificationExecutor<TodoItemPo> {
    List<TodoItemPo> findByState(String value);

    List<TodoItemPo> findByStateAndFinishTimeGreaterThanEqual(String value, Date startDate);
}
