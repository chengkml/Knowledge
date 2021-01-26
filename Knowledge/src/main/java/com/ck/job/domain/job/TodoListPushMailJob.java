package com.ck.job.domain.job;

import com.ck.todo.service.TodoItemService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;


public class TodoListPushMailJob extends QuartzJobBean {

    private static Logger LOG = LoggerFactory.getLogger(TodoListPushMailJob.class);

    @Autowired
    private TodoItemService todoServ;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            todoServ.pushListMail(-1);
        } catch (Exception e) {
            LOG.error("推送Todo邮件失败", e);
        }
    }
}
