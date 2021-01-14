package com.ck.user.dao;

import com.ck.user.po.UserPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends JpaRepository<UserPo, Long>, JpaSpecificationExecutor<UserPo> {
}
