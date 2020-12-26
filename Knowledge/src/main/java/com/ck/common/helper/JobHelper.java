package com.ck.common.helper;

import com.ck.job.domain.JobFactory;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JobHelper {

    private Scheduler scheduler;

    @Autowired
    private JobFactory jobFactory;

    private static String DEFAULT_GROUP = "default-quartz";

    private Scheduler getScheduler() throws SchedulerException {
        if (scheduler == null) {
            synchronized (JobHelper.class) {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
                scheduler.setJobFactory(jobFactory);
            }
        }
        return scheduler;
    }

    /**
     * 创建 quartz job.
     *
     * @param jobName job名字
     * @param clazz   对应的 类 字节码
     * @return jobDetail
     */
    public JobDetail createJob(String jobName, Class<? extends Job> clazz) {
        return JobBuilder.newJob(clazz).
                withIdentity(jobName, DEFAULT_GROUP).build();
    }

    /**
     * 创建 cron 触发器 (quartz)..
     * 关于cron表达式  -- <a>http://cron.qqe2.com/</a>
     *
     * @param triggerName 触发器名称
     * @param jobName     job名字 应该与 createJob 里 的第一个参数一致 {@link JobHelper#createJob}
     * @param cron        cron 表达式,
     * @return Trigger cron触发器
     */
    public Trigger createCronTrigger(String triggerName, String jobName, String cron) {
        return TriggerBuilder.newTrigger()
                .withIdentity(triggerName, DEFAULT_GROUP)
                .forJob(jobName, DEFAULT_GROUP)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
    }

    /**
     * 创建 定时任务
     *
     * @param jobName job 名字 (必须全英文)
     * @param cron    cron 表达式
     * @param clazz   ？ extend StatefulJob
     * @throws Exception 创建任务异常
     */
    public void scheduleJob(String jobName,
                            Class<? extends Job> clazz,
                            String cron
    ) throws Exception {
        Scheduler scheduler = getScheduler();

        scheduler.scheduleJob(
                createJob(jobName, clazz),
                createCronTrigger(jobName + "Trigger",
                        jobName, cron)
        );
        scheduler.start();
    }
}
