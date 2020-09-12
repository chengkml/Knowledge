package com.ck.knowledge.dao.todo;

import com.ck.knowledge.po.todo.TodoGroupPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TodoGroupRepository extends JpaRepository<TodoGroupPo, Long>, JpaSpecificationExecutor<TodoGroupPo> {
    List<TodoGroupPo> findByIdIn(List<Long> groupIds);
}
