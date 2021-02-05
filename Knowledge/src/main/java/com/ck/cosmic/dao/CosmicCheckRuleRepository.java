package com.ck.cosmic.dao;

import com.ck.cosmic.po.CosmicCheckRulePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CosmicCheckRuleRepository extends JpaRepository<CosmicCheckRulePo, Long>, JpaSpecificationExecutor<CosmicCheckRulePo> {
}
