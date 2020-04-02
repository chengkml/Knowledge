package com.ck.journal.dao;

import com.ck.journal.po.JournalPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface JournalRepository extends JpaRepository<JournalPo, Long>, JpaSpecificationExecutor<JournalPo> {
}
