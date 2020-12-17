package com.ck.menu.dao;

import com.ck.menu.po.MenuPo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface MenuRepository extends JpaRepository<MenuPo, Long>, JpaSpecificationExecutor<MenuPo> {
    List<MenuPo> findByIdIn(List<Long> menuIds);

    List<MenuPo> findByValid(String valid);
}
