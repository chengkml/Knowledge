package com.ck.knowledge.service;

import com.ck.knowledge.dao.MenuRepository;
import com.ck.knowledge.po.MenuPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuDao;

    public List<MenuPo> getMenuTree() {
        Map<Long, MenuPo> menuMap = new HashMap();
        List<MenuPo> pos = menuDao.findAll();
        pos.forEach(po -> {
            po.setSubMenus(new ArrayList<>());
            menuMap.put(po.getId(), po);
        });
        pos.forEach(po -> {
            if (po.getParentId() != null && menuMap.get(po.getParentId()) != null) {
                menuMap.get(po.getParentId()).getSubMenus().add(po);
            }
        });
        return pos.stream().filter(po -> po.getParentId()==null).collect(Collectors.toList());
    }

    public List<MenuPo> getMenuList() {
        return menuDao.findAll();
    }

    public List<MenuPo> saveMenuTree(List<MenuPo> menuTree){
        List<MenuPo> allMenuPos = new ArrayList<>();
        expandTree(menuTree,allMenuPos);
        menuDao.saveAll(menuTree);
        return menuTree;
    }

    private void expandTree(List<MenuPo> tree,List<MenuPo> all) {
        tree.forEach(node->{
            all.add(node);
            if(node.getSubMenus()!=null&&!node.getSubMenus().isEmpty()){
                expandTree(node.getSubMenus(),all);
            }
        });
    }


}
