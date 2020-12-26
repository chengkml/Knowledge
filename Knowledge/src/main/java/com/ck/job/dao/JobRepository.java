package com.ck.job.dao;

import com.ck.job.po.JobPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface JobRepository extends JpaRepository<JobPo, Long>, JpaSpecificationExecutor<JobPo> {
    List<JobPo> findByType(String value);
}
