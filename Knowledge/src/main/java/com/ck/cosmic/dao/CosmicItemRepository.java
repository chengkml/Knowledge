package com.ck.cosmic.dao;

import com.ck.cosmic.po.CosmicItemPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CosmicItemRepository extends JpaRepository<CosmicItemPo, Long>, JpaSpecificationExecutor<CosmicItemPo> {
}
