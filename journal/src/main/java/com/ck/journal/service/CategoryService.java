package com.ck.journal.service;

import com.ck.journal.dao.CategoryRepository;
import com.ck.journal.po.CategoryPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Title: CategoryService
 * @Author: Chengkai
 * @Date: 2019/8/20 13:13
 * @Version: 1.0
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository cateRepo;

    public List<CategoryPo> getCateTree(Date startTime,Date endTime) {
        List<CategoryPo> pos = cateRepo.searchDtRange(startTime,endTime);
        List<CategoryPo> tree = new ArrayList<>();
        Map<Long,CategoryPo> childrenMap = new HashMap<>();
        for(CategoryPo po : pos){
            childrenMap.put(po.getId(),po);
        }
        for(CategoryPo po : pos){
            Long temp = po.getParent();
            if(temp==null){
                tree.add(po);
            }else{
                CategoryPo sub = childrenMap.get(temp);
                if(sub!=null){
                    List<CategoryPo> tempChildren = sub.getChildren();
                    if(tempChildren==null){
                        tempChildren = new ArrayList<>();
                        sub.setChildren(tempChildren);
                    }
                    tempChildren.add(po);
                }
            }
        }
        return tree;
    }
}
