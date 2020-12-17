package com.ck.menu.service;

import com.ck.menu.dao.MenuRepository;
import com.ck.menu.enums.MenuStateEnum;
import com.ck.menu.po.MenuPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuDao;

    public List<MenuPo> getMenuTree() {
        Map<Long, MenuPo> menuMap = new HashMap();
        List<MenuPo> pos = menuDao.findByValid(MenuStateEnum.VALID.getValue());
        pos.forEach(po -> {
            po.setSubMenus(new ArrayList<>());
            menuMap.put(po.getId(), po);
        });
        pos.forEach(po -> {
            if (po.getParentId() != null && menuMap.get(po.getParentId()) != null) {
                menuMap.get(po.getParentId()).getSubMenus().add(po);
            }
        });
        pos.forEach(po -> po.getSubMenus().sort(Comparator.comparingInt(MenuPo::getSort)));
        return pos.stream().filter(po -> po.getParentId() == null).collect(Collectors.toList());
    }

    public List<MenuPo> getMenuList() {
        return menuDao.findAll();
    }

    public List<MenuPo> saveMenuTree(List<MenuPo> menuTree) {
        List<MenuPo> allMenuPos = new ArrayList<>();
        expandTree(menuTree, allMenuPos);
        menuDao.saveAll(menuTree);
        return menuTree;
    }

    private void expandTree(List<MenuPo> tree, List<MenuPo> all) {
        tree.forEach(node -> {
            all.add(node);
            if (node.getSubMenus() != null && !node.getSubMenus().isEmpty()) {
                expandTree(node.getSubMenus(), all);
            }
        });
    }

    public Long saveMenu(MenuPo menu) {
        menuDao.save(menu);
        return menu.getId();
    }

    public Integer batchDelete(List<Long> menuIds) {
        List<MenuPo> menus = menuDao.findByIdIn(menuIds);
        menuDao.deleteInBatch(menus);
        return menus.size();
    }
}
