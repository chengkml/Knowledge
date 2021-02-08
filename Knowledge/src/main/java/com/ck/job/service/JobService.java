package com.ck.job.service;

import com.ck.common.helper.JdbcQueryHelper;
import com.ck.ds.domain.DsManager;
import com.ck.job.aop.CronJob;
import com.ck.job.dao.JobRepository;
import com.ck.job.domain.QuartzScheduler;
import com.ck.job.enums.JobTypeEnum;
import com.ck.job.po.JobPo;
import org.apache.commons.collections.MapUtils;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JobService {

    private static final String DEFAULT_JOB_GROUP_NAME = "default_job_group";

    private static final String DEFAULT_TRIGGER_GROUP_NAME = "default_trigger_group";

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private QuartzScheduler quartzScheduler;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private DsManager dsManager;

    public void saveJob(JobPo po) throws SchedulerException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        Date now = new Date();
        if (po.getId() == null) {
            po.setCreateDate(now);
            quartzScheduler.addJob(po.getName(), DEFAULT_JOB_GROUP_NAME, po.getName(), DEFAULT_TRIGGER_GROUP_NAME, po.getJobClass(), po.getCron(), new HashMap<>());
        } else {
            quartzScheduler.modifyJob(po.getName(), DEFAULT_TRIGGER_GROUP_NAME, po.getCron());
        }
        po.setLastUpdDate(now);
        jobRepo.save(po);
    }

    public void deleteJob(Long id) throws SchedulerException {
        JobPo po = jobRepo.getOne(id);
        quartzScheduler.deleteJob(po.getName(), DEFAULT_JOB_GROUP_NAME);
        jobRepo.delete(po);
    }

    public Page<JobPo> search(int pageNum, int pageSize, String keyWord) {
        StringBuilder listSql = new StringBuilder("select j.id, j.create_date, j.cron, j.label, j.last_upd_date, j.name, j.type,j.job_class,j.cron from ck_job j where 1=1 ");
        StringBuilder countSql = new StringBuilder("select count(1) from ck_job j where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        JdbcQueryHelper.lowerLike("keyWord", keyWord, "and (lower(j.name) like :keyWord or lower(j.label) like :keyWord) ", params, dsManager.getLocalDsType(), listSql, countSql);
        JdbcQueryHelper.order("create_date", "desc", listSql);
        List<JobPo> list = new ArrayList<>();
        dsManager.getNamedJdbcTemplate().queryForList(JdbcQueryHelper.getLimitSql(dsManager.getNamedJdbcTemplate(), listSql, pageNum, pageSize), params).forEach(map -> {
            JobPo job = new JobPo();
            job.setId(MapUtils.getLong(map, "id"));
            job.setName(MapUtils.getString(map, "name"));
            job.setLabel(MapUtils.getString(map, "label"));
            job.setType(MapUtils.getString(map, "type"));
            job.setJobClass(MapUtils.getString(map, "job_class"));
            job.setCron(MapUtils.getString(map, "cron"));
            job.setCreateDate((Date) MapUtils.getObject(map, "create_date"));
            job.setLastUpdDate((Date) MapUtils.getObject(map, "last_upd_date"));
            list.add(job);
        });
        return JdbcQueryHelper.toPage(dsManager.getNamedJdbcTemplate(), countSql, params, list, pageNum, pageSize);
    }

    public int syncAll() throws SchedulerException, IllegalAccessException, InstantiationException, ClassNotFoundException {
        List<JobPo> pos = jobRepo.findByType(JobTypeEnum.CRON.getValue());
        for (JobPo po : pos) {
            TriggerKey triggerKey = new TriggerKey(po.getName(), DEFAULT_TRIGGER_GROUP_NAME);
            if (scheduler.getTrigger(triggerKey) != null) {
                quartzScheduler.modifyJob(po.getName(), DEFAULT_TRIGGER_GROUP_NAME, po.getCron());
            } else {
                quartzScheduler.addJob(po.getName(), DEFAULT_JOB_GROUP_NAME, po.getName(), DEFAULT_TRIGGER_GROUP_NAME, po.getJobClass(), po.getCron(), new HashMap<>());
            }
        }
        return pos.size();
    }

    public List<Map<String, String>> scanJobClass() {
        List<Map<String, String>> res = new ArrayList<>();
        Reflections f = new Reflections("com.ck.job.domain.job");
        Set<Class<?>> jobClasses = f.getTypesAnnotatedWith(CronJob.class);
        for (Class<?> jobClass : jobClasses) {
            CronJob cj = jobClass.getAnnotation(CronJob.class);
            Map<String, String> map = new HashMap<>();
            map.put("label", cj.value());
            map.put("value", jobClass.getTypeName());
            res.add(map);
        }
        return res;
    }

    public void fireJob(Long jobId) throws SchedulerException {
        JobPo job = jobRepo.getOne(jobId);
        quartzScheduler.runJobNow(job.getName(),DEFAULT_JOB_GROUP_NAME);
    }
}
