package com.ck.job.domain.job;

import com.ck.job.aop.CronJob;
import com.ck.res.service.StaticResService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@CronJob("资源目录清理定时任务")
public class ResDirCleanJob extends QuartzJobBean {

    private static Logger LOG = LoggerFactory.getLogger(ResDirCleanJob.class);

    @Autowired
    private StaticResService resServ;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try {
            resServ.cleanDirFromRoot();
        } catch (Exception e) {
            LOG.error("清理资源目录异常！", e);
        }
    }

}
