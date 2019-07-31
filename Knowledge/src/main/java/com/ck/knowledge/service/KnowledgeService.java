package com.ck.knowledge.service;

import com.ck.knowledge.dao.CategoryRepository;
import com.ck.knowledge.dao.KnowledgeRepository;
import com.ck.knowledge.po.CategoryPo;
import com.ck.knowledge.po.KnowledgePo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * @Title: KnowledgeService
 * @Description: TODO
 * @Author: Chengkai
 * @Date: 2019/6/19 23:29
 * @Version: 1.0
 */
@Service
public class KnowledgeService {

    @Autowired
    private KnowledgeRepository repo;

    @Autowired
    private CategoryRepository cateRepo;

    @Autowired
    private CategoryService cateServ;

    public Page<KnowledgePo> search(int pageSize, int pageNum, String keyWord,long category){
        Sort sort = new Sort(Sort.Direction.DESC,"createDate");
        Pageable page = PageRequest.of(pageSize,pageNum,sort);
        Specification  sf = new Specification<KnowledgePo>(){
            @Override
            public Predicate toPredicate(Root<KnowledgePo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(StringUtils.isNotBlank(keyWord)){
                    List<Predicate> subPredicates = new ArrayList<>();
                    subPredicates.add(cb.like(root.<String>get("name"),"%"+keyWord+"%"));
                    subPredicates.add(cb.like(root.<String>get("descr"),"%"+keyWord+"%"));
                    predicates.add(cb.or(subPredicates.toArray(new Predicate[subPredicates.size()])));
                }
                if(category>=0){
                    List<Long> ids = cateServ.getSubIds(category);
                    CriteriaBuilder.In<Long> in = cb.in(root.<Long>get("category"));
                    for(Long id : ids){
                        in.value(id);
                    }
                    predicates.add(in);
                }
                return cq.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        };
        return repo.findAll(sf,page);
    }

    public List<CategoryPo> getKnowledgeTree(long nodeId) {
        List<CategoryPo> children = new ArrayList<>();
        List<CategoryPo> pos = cateRepo.findAll();
        List<KnowledgePo> kPos = repo.findAll();
        Map<Long, List<CategoryPo>> kpoMap = new HashMap<>();
        List<CategoryPo> tempList = null;
        for(KnowledgePo kpo : kPos){
            tempList = kpoMap.computeIfAbsent(kpo.getCategory(),k -> new ArrayList<>());
            tempList.add(new CategoryPo(kpo.getId(),kpo.getName(),kpo.getName()));
        }
        Map<Long,CategoryPo> cgMap = new HashMap<>();
        for(CategoryPo po : pos){
            po.setChildren(kpoMap.get(po.getId()));
            children.add(po);
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
        return Arrays.asList(new CategoryPo[]{cgMap.get(nodeId)});
    }

    public List<CategoryPo> getKnowledgeTree() {
        List<CategoryPo> children = new ArrayList<>();
        List<CategoryPo> root = new ArrayList<>();
        List<CategoryPo> pos = cateRepo.findAll();
        List<KnowledgePo> kPos = repo.findAll();
        Map<Long, List<CategoryPo>> kpoMap = new HashMap<>();
        List<CategoryPo> tempList = null;
        for(KnowledgePo kpo : kPos){
            tempList = kpoMap.computeIfAbsent(kpo.getCategory(),k -> new ArrayList<>());
            tempList.add(new CategoryPo(kpo.getId(),kpo.getName(),kpo.getName()));
        }
        Map<Long,CategoryPo> cgMap = new HashMap<>();
        for(CategoryPo po : pos){
            po.setChildren(kpoMap.get(po.getId()));
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
}
