package com.ck.knowledge.service;

import com.ck.knowledge.dao.todo.TodoGroupRepository;
import com.ck.knowledge.po.todo.TodoGroupPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TodoGroupService {

    @Autowired
    private TodoGroupRepository repo;

    public List<TodoGroupPo> getTree() {
        List<TodoGroupPo> children = new ArrayList<>();
        List<TodoGroupPo> root = new ArrayList<>();
        List<TodoGroupPo> pos = repo.findAll();
        Map<Long, TodoGroupPo> cgMap = new HashMap<>();
        for (TodoGroupPo po : pos) {
            if (po.getParentId() == null) {
                root.add(po);
            } else {
                children.add(po);
            }
            cgMap.put(po.getId(), po);
        }
        for (TodoGroupPo cg : children) {
            Long parentId = cg.getParentId();
            TodoGroupPo parent = cgMap.get(parentId);
            if (parent != null) {
                List<TodoGroupPo> sub = parent.getChildren();
                if (sub == null) {
                    sub = new ArrayList<>();
                    parent.setChildren(sub);
                }
                sub.add(cg);
            }
        }
        return root;
    }

    public List<Long> getSubIds(long id) {
        List<TodoGroupPo> roots = getTree();
        Set<Long> res = new HashSet<>();
        res.add(id);
        selectSub(roots, res);
        return new ArrayList<>(res);
    }

    private void selectSub(List<TodoGroupPo> roots, Set<Long> res) {
        if (roots != null && !roots.isEmpty()) {
            for (TodoGroupPo po : roots) {
                if (res.contains(po.getParentId())) {
                    res.add(po.getId());
                }
                if (po.getChildren() != null && !po.getChildren().isEmpty()) {
                    selectSub(po.getChildren(), res);
                }
            }
        }
    }

    public Object groupTree() {
        Map<Long, TodoGroupPo> groupMap = new HashMap();
        List<TodoGroupPo> pos = repo.findAll();
        pos.forEach(po -> {
            groupMap.put(po.getId(), po);
        });
        pos.forEach(po -> {
            if (po.getParentId() != null && groupMap.get(po.getParentId()) != null) {
                if(groupMap.get(po.getParentId()).getChildren()==null){
                    groupMap.get(po.getParentId()).setChildren(new ArrayList<>());
                }
                groupMap.get(po.getParentId()).getChildren().add(po);
            }
        });
        pos.forEach(po -> po.getChildren());
        return pos.stream().filter(po -> po.getParentId() == null).collect(Collectors.toList());
    }

    public Object addGroup(TodoGroupPo group) {
        return repo.save(group);
    }

    public Object deleteGroup(List<Long> groupIds) {
        List<TodoGroupPo> groups = repo.findByIdIn(groupIds);
        repo.deleteInBatch(groups);
        return groups.size();
    }
}
