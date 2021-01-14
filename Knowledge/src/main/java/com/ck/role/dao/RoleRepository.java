package com.ck.role.dao;

import com.ck.role.po.RolePo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleRepository extends JpaRepository<RolePo, Long>, JpaSpecificationExecutor<RolePo> {
}
