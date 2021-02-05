package com.ck.cosmic.service;

import com.ck.common.helper.JdbcQueryHelper;
import com.ck.cosmic.dao.CosmicCheckRuleRepository;
import com.ck.cosmic.po.CosmicCheckRulePo;
import com.ck.cosmic.vo.CosmicCheckRule;
import com.ck.ds.domain.DsManager;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CosmicCheckRuleService {

    @Autowired
    private DsManager dsManager;

    @Autowired
    private CosmicCheckRuleRepository ruleRepo;

    public List<CosmicCheckRule> getRules() {
        List<CosmicCheckRule> rules = new ArrayList<>();
        List<CosmicCheckRulePo> pos = ruleRepo.findAll();
        for (CosmicCheckRulePo po : pos) {
            CosmicCheckRule rule = new CosmicCheckRule();
            rule.setSortNum(po.getSortNum());
            rule.setElement(po.getElement());
            rule.setRule(po.getRule());
            rule.setId(po.getId());
            rules.add(rule);
        }
        return rules;
    }

    public List<CosmicCheckRule> saveRules(List<CosmicCheckRule> rules) {
        ruleRepo.deleteAll();
        List<CosmicCheckRulePo> pos = new ArrayList<>();
        Date now = new Date();
        for (CosmicCheckRule rule : rules) {
            CosmicCheckRulePo po = new CosmicCheckRulePo();
            po.setSortNum(rule.getSortNum());
            po.setElement(rule.getElement());
            po.setRule(rule.getRule());
            po.setCreateDate(now);
            pos.add(po);
        }
        ruleRepo.saveAll(pos);
        return rules;
    }

    public Page<CosmicCheckRule> searchRules(String keyWord, int pageNum, int pageSize) {
        List<CosmicCheckRule> rules = new ArrayList<>();
        StringBuilder listSql = new StringBuilder("select r.id, r.create_date, r.create_user, r.element, r.rule, r.sort_num from ck_cosmic_check_rule r where 1=1 ");
        StringBuilder countSql = new StringBuilder(" select count(id) from ck_cosmic_check_rule r where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        JdbcQueryHelper.lowerLike("keyWord", keyWord, "and (lower(r.element) like :keyWord or lower(r.rule) like :keyWord) ",
                params, dsManager.getLocalDsType(), listSql, countSql);
        NamedParameterJdbcTemplate jt = dsManager.getNamedJdbcTemplate();
        jt.queryForList(JdbcQueryHelper.getLimitSql(jt, listSql, pageNum, pageSize), params).forEach(map -> {
            CosmicCheckRule rule = new CosmicCheckRule();
            rule.setSortNum(MapUtils.getIntValue(map, "sort_num"));
            rule.setElement(MapUtils.getString(map, "element"));
            rule.setRule(MapUtils.getString(map, "rule"));
            rule.setId(MapUtils.getLong(map, "id"));
            rules.add(rule);
        });
        return JdbcQueryHelper.toPage(jt, countSql, params, rules, pageNum, pageSize);
    }

    public CosmicCheckRule saveRule(CosmicCheckRule rule) {
        CosmicCheckRulePo po;
        if (rule.getId() == null) {
            po = new CosmicCheckRulePo();
        } else {
            po = ruleRepo.getOne(rule.getId());
        }
        po.setSortNum(rule.getSortNum());
        po.setElement(rule.getElement());
        po.setRule(rule.getRule());
        po.setCreateDate(new Date());
        ruleRepo.save(po);
        return rule;
    }

    public int deleteRules(List<Long> ruleIds) {
        List<CosmicCheckRulePo> pos = ruleRepo.findAllById(ruleIds);
        ruleRepo.deleteAll(pos);
        return pos.size();
    }
}
