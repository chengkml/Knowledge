package com.ck.cosmic.dao;

import com.ck.cosmic.po.CosmicDemandPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CosmicDemandRepository extends JpaRepository<CosmicDemandPo, Long>, JpaSpecificationExecutor<CosmicDemandPo> {
}
