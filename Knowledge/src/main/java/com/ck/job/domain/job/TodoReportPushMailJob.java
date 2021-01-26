package com.ck.job.domain.job;

import com.ck.todo.service.TodoItemService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Calendar;
import java.util.Date;

public class TodoReportPushMailJob extends QuartzJobBean {

    private static Logger LOG = LoggerFactory.getLogger(TodoReportPushMailJob.class);

    @Autowired
    private TodoItemService todoServ;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -1);
            todoServ.pushFinishedItem(cal.getTime());
        } catch (Exception e) {
            LOG.error("推送Todo报告邮件失败", e);
        }
    }
}