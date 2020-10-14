package com.ck.knowledge.dao.bat;

import com.ck.knowledge.po.bat.BatPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BatRepository extends JpaRepository<BatPo, Long>, JpaSpecificationExecutor<BatPo> {
}
