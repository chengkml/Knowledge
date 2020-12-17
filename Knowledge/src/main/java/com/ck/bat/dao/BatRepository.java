package com.ck.bat.dao;

import com.ck.bat.po.BatPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BatRepository extends JpaRepository<BatPo, Long>, JpaSpecificationExecutor<BatPo> {
}
