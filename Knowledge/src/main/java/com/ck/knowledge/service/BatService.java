package com.ck.knowledge.service;

import com.ck.knowledge.dao.bat.BatRepository;
import com.ck.knowledge.dao.res.StaticResRepository;
import com.ck.knowledge.po.bat.BatPo;
import com.ck.knowledge.po.res.StaticResPo;
import com.ck.knowledge.po.todo.TodoItemPo;
import com.ck.knowledge.websocket.CkWebSocketHandler;
import com.ck.knowledge.websocket.wo.BatLog;
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
import java.io.FileInputStream;
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

    public void start(Long batId) {
        String line;
        List<StaticResPo> ress = resRepo.findByRelaId(batId);
        if (ress.isEmpty()) {
            throw new RuntimeException("脚本文件不存在！");
        }



        StringBuilder sb = new StringBuilder();
        try (BufferedReader batReader = new BufferedReader(new InputStreamReader(new FileInputStream(ress.get(0).getPath()),"gbk"))) {
            while ((line = batReader.readLine()) != null) {
                sb.append(line + "\n");
            }
            ProcessBuilder builder = new ProcessBuilder(sb.toString(), "jituandyijing");
            Process process = builder.start();
            try (BufferedReader logReader = new BufferedReader(new InputStreamReader(process.getInputStream(),"gbk"))) {
                while ((line = logReader.readLine()) != null) {
                    CkWebSocketHandler.sendMsgToAll(new BatLog(line + "\n"));
                }
//                process .waitFor();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
