package com.ck.knowledge.service;

import com.ck.knowledge.dao.bat.BatRepository;
import com.ck.knowledge.dao.res.StaticResRepository;
import com.ck.knowledge.po.bat.BatPo;
import com.ck.knowledge.po.res.StaticResPo;
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

import javax.persistence.criteria.Predicate;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class BatService {

    @Autowired
    private BatRepository batRepo;

    @Autowired
    private StaticResService resServ;

    @Autowired
    private StaticResRepository resRepo;

    public void exe() throws IOException {
        StringBuilder sb = new StringBuilder();
        Process child = Runtime.getRuntime().exec("C:\\Users\\Administrator\\Desktop\\tt.bat");
        InputStream in = child.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line + "\n");
        }
        in.close();
        try {
            child.waitFor();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        System.out.println("sb:" + sb.toString());
        System.out.println("callCmd execute finished");
    }

    public Long saveBat(BatPo batPo) {
        if (batPo.getId() == null) {
            batPo.setCreateDate(new Date());
        }
        batRepo.save(batPo);
        if (batPo.getFileId() != null) {
            resServ.matchRes(batPo.getId(), Arrays.asList(batPo.getFileId()));
        }
        return batPo.getId();
    }

    @Transactional
    public Long deleteBat(Long batId) {
        BatPo batPo = batRepo.getOne(batId);
        batRepo.delete(batPo);
        resServ.deleteByRelaId(batId);
        return batId;
    }

    public Page<BatPo> list(String keyWord, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Specification sf = (Specification<TodoItemPo>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(keyWord)) {
                List<Predicate> subPredicates = new ArrayList<>();
                subPredicates.add(cb.or(cb.like(root.get("name"), "%" + keyWord + "%"), cb.like(root.get("label"), "%" + keyWord + "%")));
                predicates.add(cb.or(subPredicates.toArray(new Predicate[subPredicates.size()])));
            }
            return cq.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<BatPo> pageRes = batRepo.findAll(sf, page);
        pageRes.getContent().forEach(bat -> {
            List<StaticResPo> resPos = resRepo.findByRelaId(bat.getId());
            if (!resPos.isEmpty()) {
                bat.setBat(resPos.get(0));
            }
        });
        return pageRes;
    }
}
