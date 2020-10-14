package com.ck.knowledge.dao.bat;

import com.ck.knowledge.po.bat.BatParamPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BatParamRepository extends JpaRepository<BatParamPo, Long>, JpaSpecificationExecutor<BatParamPo> {

    List<BatParamPo> findByBatId(Long batId);
}
