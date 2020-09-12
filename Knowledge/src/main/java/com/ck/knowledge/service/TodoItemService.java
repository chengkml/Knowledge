package com.ck.knowledge.service;

import com.ck.knowledge.dao.todo.TodoAnalysisRepository;
import com.ck.knowledge.dao.todo.TodoItemRepository;
import com.ck.knowledge.enums.TodoItemStateEnum;
import com.ck.knowledge.po.todo.TodoAnalysisPo;
import com.ck.knowledge.po.todo.TodoGroupPo;
import com.ck.knowledge.po.todo.TodoItemPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TodoItemService {

    @Autowired
    private TodoGroupService groupServ;

    @Autowired
    private TodoItemRepository repo;

    @Autowired
    private TodoAnalysisRepository analysisRepo;

    public Page<TodoItemPo> list(List<String> states, String keyWord, long group, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Specification sf = (Specification<TodoItemPo>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(states!=null&&!states.isEmpty()){
                CriteriaBuilder.In<String> in = cb.in(root.get("state"));
                for (String state : states) {
                    in.value(state);
                }
                predicates.add(in);
            }
            if (StringUtils.isNotBlank(keyWord)) {
                List<Predicate> subPredicates = new ArrayList<>();
                subPredicates.add(cb.like(root.get("name"), "%" + keyWord + "%"));
                predicates.add(cb.or(subPredicates.toArray(new Predicate[subPredicates.size()])));
            }
            if (group >= 0) {
                List<Long> ids = groupServ.getSubIds(group);
                CriteriaBuilder.In<Long> in = cb.in(root.get("groupId"));
                for (Long id : ids) {
                    in.value(id);
                }
                predicates.add(in);
            }
            return cq.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<TodoItemPo> pageRes = repo.findAll(sf, page);
        return pageRes;
    }

    @Transactional
    public Object save(TodoItemPo item) {
        if(!TodoItemStateEnum.FINISH.getValue().equals(item.getState())&&item.getEstimateStartTime().getTime()<new Date().getTime()){
            item.setState(TodoItemStateEnum.RUNNING.getValue());
        }else{
            item.setState(TodoItemStateEnum.WAITING.getValue());
        }
        item.setLastUpdDate(new Date());
        repo.save(item);
        List<TodoAnalysisPo> analyses = item.getAnalyses();
        if (analyses != null && !analyses.isEmpty()) {
            analysisRepo.saveAll(analyses);
        }
        return item.getId();
    }

    @Transactional
    public Object delete(long id) {
        List<TodoAnalysisPo> analyses = analysisRepo.findByTodoId(id);
        analysisRepo.deleteAll(analyses);
        repo.deleteById(id);
        return id;
    }

}
