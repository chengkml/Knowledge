package com.ck.ds.dao;

import com.ck.ds.po.DsPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DsRepository extends JpaRepository<DsPo, Long>, JpaSpecificationExecutor<DsPo> {

    DsPo findByName(String dsName);

}
