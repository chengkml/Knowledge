package com.ck.job.domain.job;

import com.ck.exercise.service.ExerciseService;
import com.ck.job.aop.CronJob;
import lombok.SneakyThrows;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

@CronJob("知识点练习定时任务")
public class KnowledgeExerciseJob extends QuartzJobBean {

    private static final int EXERCISE_SIZE = 20;

    @Autowired
    private ExerciseService exerciseService;

    @SneakyThrows
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        exerciseService.generateExercise(EXERCISE_SIZE);
    }
}
