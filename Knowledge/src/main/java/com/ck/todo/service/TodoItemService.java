package com.ck.todo.service;

import com.ck.common.helper.JdbcQueryHelper;
import com.ck.common.helper.TemplateHelper;
import com.ck.ds.domain.DsManager;
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
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @Autowired
    private DsManager dsManager;

    public Page<TodoItemPo> list(List<String> states, String keyWord, long group, int pageNum, int pageSize) {
        StringBuilder listSql = new StringBuilder("select i.id, i.create_date, i.create_user, i.group_id, i.last_upd_date, i.name, i.lead_time, i.estimate_end_time," +
                " i.estimate_start_time, i.state, i.finish_time, i.analysis from ck_todo_item i where 1=1 ");
        StringBuilder countSql = new StringBuilder("select count(1) from ck_todo_item i where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        JdbcQueryHelper.in("states", states, "and i.state in (:states) ", params, listSql, countSql);
        JdbcQueryHelper.lowerLike("keyWord", keyWord, "and lower(i.name) like :keyWord ", params, dsManager.getLocalDsType(), listSql, countSql);
        if (group > 0) {
            List<Long> ids = groupServ.getSubIds(group);
            JdbcQueryHelper.in("groupIds", ids, "and i.group_id in (:groupIds) ", params, listSql, countSql);
        }
        JdbcQueryHelper.order("create_date", "desc", listSql);
        List<TodoItemPo> list = new ArrayList<>();
        dsManager.getNamedJdbcTemplate().queryForList(JdbcQueryHelper.getLimitSql(dsManager.getNamedJdbcTemplate(), listSql, pageNum, pageSize), params).forEach(map -> {
            TodoItemPo item = new TodoItemPo();
            item.setId(MapUtils.getLong(map, "id"));
            item.setCreateDate((Date) MapUtils.getObject(map, "create_date"));
            item.setCreateUser(MapUtils.getLong(map, "create_user"));
            item.setGroupId(MapUtils.getLong(map, "group_id"));
            item.setLastUpdDate((Date) MapUtils.getObject(map, "last_upd_date"));
            item.setName(MapUtils.getString(map, "name"));
            item.setLeadTime((Date) MapUtils.getObject(map, "lead_time"));
            item.setEstimateEndTime((Date) MapUtils.getObject(map, "estimate_end_time"));
            item.setEstimateStartTime((Date) MapUtils.getObject(map, "estimate_start_time"));
            item.setState(MapUtils.getString(map, "state"));
            item.setFinishTime((Date) MapUtils.getObject(map, "finish_time"));
            list.add(item);
        });
        return JdbcQueryHelper.toPage(dsManager.getNamedJdbcTemplate(), countSql, params, list, pageNum, pageSize);
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
