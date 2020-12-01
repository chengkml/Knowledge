package com.ck.knowledge.dao.res;

import com.ck.knowledge.po.res.StaticResPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.ArrayList;
import java.util.List;

public interface StaticResRepository extends JpaRepository<StaticResPo, Long>, JpaSpecificationExecutor<StaticResPo> {

    List<StaticResPo> findByValid(String value);

    List<StaticResPo> findByRelaId(Long relaId);

    List<StaticResPo> findByMdCodeIn(List<String> mdCodes);

    List<StaticResPo> findByMdCode(String mdCode);
}
