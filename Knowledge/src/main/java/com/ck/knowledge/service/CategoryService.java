package com.ck.knowledge.service;

import com.ck.knowledge.dao.CategoryRepository;
import com.ck.knowledge.po.CategoryPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Title: CategoryService
 * @Author: Chengkai
 * @Date: 2019/6/19 23:07
 * @Version: 1.0
 */
@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;

    public List<CategoryPo> getTree(){
        List<CategoryPo> children = new ArrayList<>();
        List<CategoryPo> root = new ArrayList<>();
        List<CategoryPo> pos = repo.findAll();
        Map<Long,CategoryPo> cgMap = new HashMap<>();
        for(CategoryPo po : pos){
            if(po.getParentId()==null){
                root.add(po);
            }else{
                children.add(po);
            }
            cgMap.put(po.getId(),po);
        }
        for(CategoryPo cg : children){
            Long parentId = cg.getParentId();
            CategoryPo parent = cgMap.get(parentId);
            if(parent!=null){
                List<CategoryPo> sub = parent.getChildren();
                if(sub==null){
                    sub = new ArrayList<>();
                    parent.setChildren(sub);
                }
                sub.add(cg);
            }
        }
        return root;
    }

    public List<Long> getSubIds(long id){
        List<CategoryPo> roots = getTree();
        Set<Long> res = new HashSet<>();
        res.add(id);
        selectSub(roots,res);
        return new ArrayList<>(res);
    }

    private void selectSub(List<CategoryPo> roots, Set<Long> res){
        if(roots!=null&&!roots.isEmpty()){
            for(CategoryPo po : roots){
                if(res.contains(po.getParentId())){
                    res.add(po.getId());
                }
                if(po.getChildren()!=null&&!po.getChildren().isEmpty()){
                    selectSub(po.getChildren(),res);
                }
            }
        }
    }
}
