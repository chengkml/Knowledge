package com.ck.job.service;

import com.ck.job.dao.JobRepository;
import com.ck.job.domain.QuartzScheduler;
import com.ck.job.enums.JobTypeEnum;
import com.ck.job.po.JobPo;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class JobService {

    private static final String DEFAULT_JOB_GROUP_NAME = "default_job_group";

    private static final String DEFAULT_TRIGGER_GROUP_NAME = "default_trigger_group";

    @Autowired
    private JobRepository jobRepo;

    @Autowired
    private QuartzScheduler quartzScheduler;

    public void saveJob(JobPo po) throws SchedulerException {
        if (po.getId() == null) {
            quartzScheduler.addJob(po.getName(), DEFAULT_JOB_GROUP_NAME, po.getName(), DEFAULT_TRIGGER_GROUP_NAME, "", po.getCron(), new HashMap<>());
        } else {
            quartzScheduler.modifyJob(po.getName(), DEFAULT_TRIGGER_GROUP_NAME, po.getCron());
        }
        jobRepo.save(po);
    }

    public void deleteJob(Long id) throws SchedulerException {
        JobPo po = jobRepo.getOne(id);
        quartzScheduler.deleteJob(po.getName(), DEFAULT_JOB_GROUP_NAME);
        jobRepo.delete(po);
    }

    public Page<JobPo> search(int pageNum, int pageSize, String keyWord) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Specification sf = (Specification<JobPo>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(keyWord)) {
                List<Predicate> subPredicates = new ArrayList<>();
                subPredicates.add(cb.like(root.get("name"), "%" + keyWord + "%"));
                subPredicates.add(cb.like(root.get("label"), "%" + keyWord + "%"));
                predicates.add(cb.or(subPredicates.toArray(new Predicate[subPredicates.size()])));
            }
            return cq.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<JobPo> pageRes = jobRepo.findAll(sf, page);
        return pageRes;
    }

    public int syncAll() throws SchedulerException {
        List<JobPo> pos = jobRepo.findByType(JobTypeEnum.CRON.getValue());
        for (JobPo po : pos) {
            quartzScheduler.modifyJob(po.getName(), DEFAULT_TRIGGER_GROUP_NAME, po.getCron());
        }
        return pos.size();
    }

}
