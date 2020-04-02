package com.ck.knowledge.dao;

import com.ck.knowledge.po.CategoryPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends JpaRepository<CategoryPo,Long>, JpaSpecificationExecutor<CategoryPo> {
}
