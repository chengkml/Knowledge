package com.ck.job.domain;

import com.ck.job.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value = 1)
public class CronJobLoader implements ApplicationRunner {

    @Autowired
    private JobService jobServ;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        jobServ.syncAll();
    }
}
