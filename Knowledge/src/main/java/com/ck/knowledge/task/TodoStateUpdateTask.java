package com.ck.knowledge.task;

import com.ck.knowledge.dao.todo.TodoItemRepository;
import com.ck.knowledge.enums.TodoItemStateEnum;
import com.ck.knowledge.po.todo.TodoItemPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoStateUpdateTask {

    @Autowired
    private TodoItemRepository itemRepo;

    private void configureTasks() {
        long now = new Date().getTime();
        List<TodoItemPo> toUpdate = new ArrayList<>();
        itemRepo.findByState(TodoItemStateEnum.WAITING.getValue()).forEach(po -> {
            if (po.getEstimateStartTime() != null && po.getEstimateStartTime().getTime() < now) {
                po.setState(TodoItemStateEnum.RUNNING.getValue());
                toUpdate.add(po);
            }
        });
        itemRepo.saveAll(toUpdate);
    }

}
