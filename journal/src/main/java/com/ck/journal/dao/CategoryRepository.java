package com.ck.journal.dao;

import com.ck.journal.po.CategoryPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @Title: CategoryDao
 * @Author: Chengkai
 * @Date: 2019/8/20 13:01
 * @Version: 1.0
 */
@Repository
public interface CategoryRepository extends JpaRepository<CategoryPo, Long>, JpaSpecificationExecutor<CategoryPo> {

    @Query("from CategoryPo p where p.createDt>=:startTime and p.createDt<:endTime order by createDt asc")
    List<CategoryPo> searchDtRange(@Param("startTime") Date startTime,@Param("endTime") Date endTime);

    @Query("from CategoryPo p where p.type=:type order by createDt asc")
    List<CategoryPo> searchByType(@Param("type") String type);

    @Query("from CategoryPo p where p.type=:type and p.value=:value")
    CategoryPo findYearByValue(@Param("type") String type,@Param("value") int value);

    @Query("from CategoryPo p where p.type=:type and p.value=:value and p.parent = :parent")
    CategoryPo findByTypeAndValue(@Param("type") String type,@Param("value") int value,@Param("parent") Long parent);
}
