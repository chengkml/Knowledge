package com.ck.job.domain;

import com.ck.todo.dao.TodoItemRepository;
import com.ck.todo.enums.TodoItemStateEnum;
import com.ck.todo.po.TodoItemPo;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoStateUpdateJob extends QuartzJobBean {

    @Autowired
    private TodoItemRepository itemRepo;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
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
