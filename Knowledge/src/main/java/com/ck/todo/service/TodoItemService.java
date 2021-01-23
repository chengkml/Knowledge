package com.ck.todo.service;

import com.ck.common.helper.TemplateHelper;
import com.ck.mail.service.MailService;
import com.ck.res.dao.StaticResRepository;
import com.ck.res.enums.ResValidEnum;
import com.ck.res.po.StaticResPo;
import com.ck.res.service.StaticResService;
import com.ck.todo.dao.TodoGroupRepository;
import com.ck.todo.dao.TodoItemRepository;
import com.ck.todo.enums.TodoItemStateEnum;
import com.ck.todo.po.TodoItemPo;
import com.ck.todo.vo.TodoItemVo;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class TodoItemService {

    @Autowired
    private TodoGroupService groupServ;

    @Autowired
    private TodoItemRepository repo;

    @Autowired
    private MailService mailService;

    @Autowired
    private TodoGroupRepository groupRepo;

    @Autowired
    private StaticResService resServ;

    @Autowired
    private StaticResRepository resRepo;

    public Page<TodoItemPo> list(List<String> states, String keyWord, long group, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Specification sf = (Specification<TodoItemPo>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (states != null && !states.isEmpty()) {
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
        item.setState(updateItemStateByTime(item.getState(), item.getEstimateStartTime()));
        item.setLastUpdDate(new Date());
        repo.save(item);
        List<String> mds = updateInvalidRes(item.getId(), item.getAnalysis());
        if (item.getResIds() != null && !item.getResIds().isEmpty()) {
            List<Long> tempIds = resRepo.findAllById(item.getResIds()).stream().filter(resPo -> mds.contains(resPo.getMdCode())).map(resPo -> resPo.getId()).collect(Collectors.toList());
            resServ.matchRes(item.getId(), tempIds);
        }
        return item.getId();
    }

    private List<String> updateInvalidRes(Long relaId, String analysis) {
        List<String> mds = new ArrayList<>();
        if (StringUtils.isNotBlank(analysis)) {
            Pattern p = Pattern.compile("\\<img src=\\\"[\\S]+\\.");
            Matcher m = p.matcher(analysis);
            while (m.find()) {
                String temp = m.group();
                mds.add(temp.substring(10, temp.length() - 1));
            }
        }
        List<StaticResPo> resPos = resRepo.findByRelaId(relaId);
        resPos.forEach(resPo -> {
            if (!mds.contains(resPo.getMdCode())) {
                resPo.setRelaId(null);
                resPo.setValid(ResValidEnum.INVALID.getValue());
            }
        });
        resRepo.saveAll(resPos);
        return mds;
    }

    /**
     * 根据项目状态和预估开始时间更新项目状态
     *
     * @param itemState         项目状态
     * @param estimateStartTime 项目预估开始时间
     * @return 项目状态
     */
    private String updateItemStateByTime(String itemState, Date estimateStartTime) {
        if (!TodoItemStateEnum.FINISH.getValue().equals(itemState) && estimateStartTime.getTime() < new Date().getTime()) {
            return TodoItemStateEnum.RUNNING.getValue();
        } else if (!TodoItemStateEnum.FINISH.getValue().equals(itemState)) {
            return TodoItemStateEnum.WAITING.getValue();
        }
        return itemState;
    }

    @Transactional
    public Object delete(long id) {
        repo.deleteById(id);
        return id;
    }

    public int generateReport(int size) throws IOException, TemplateException, MessagingException {
        Map<Long, String> groupMap = new HashMap<>();
        groupRepo.findAll().forEach(po -> {
            groupMap.put(po.getId(), po.getDescr());
        });
        List<TodoItemVo> todoItems = new ArrayList<>();
        Specification sf = (Specification<TodoItemPo>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            CriteriaBuilder.In<String> in = cb.in(root.get("state"));
            in.value(TodoItemStateEnum.WAITING.getValue());
            in.value(TodoItemStateEnum.RUNNING.getValue());
            predicates.add(in);
            return cq.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        if (size == -1) {
            List<TodoItemPo> pos = repo.findAll(sf);
            for (TodoItemPo po : pos) {
                TodoItemVo vo = new TodoItemVo();
                BeanUtils.copyProperties(po, vo);
                vo.setGroup(groupMap.get(po.getGroupId()));
                todoItems.add(vo);
            }
        } else {
            Page<TodoItemPo> todoItemPage = repo.findAll(sf, PageRequest.of(0, size, new Sort(Sort.Direction.DESC, "createDate")));
            List<TodoItemPo> pos = todoItemPage.getContent();
            for (TodoItemPo po : pos) {
                TodoItemVo vo = new TodoItemVo();
                BeanUtils.copyProperties(po, vo);
                vo.setGroup(groupMap.get(po.getGroupId()));
                todoItems.add(vo);
            }
        }
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("todoItems", todoItems);
        StringWriter sw = new StringWriter();
        Template template = TemplateHelper.getTemplate(TemplateHelper.TODO_TPL);
        template.process(dataMap, sw);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        mailService.sendHTMLMail(sdf.format(new Date()) + "工作计划", sw.toString());
        return todoItems.size();
    }
}
