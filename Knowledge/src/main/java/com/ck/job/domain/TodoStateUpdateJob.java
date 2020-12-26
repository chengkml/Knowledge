package com.ck.job.domain;

import com.ck.todo.dao.TodoItemRepository;
import com.ck.todo.enums.TodoItemStateEnum;
import com.ck.todo.po.TodoItemPo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoStateUpdateJob extends CronJob {

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
