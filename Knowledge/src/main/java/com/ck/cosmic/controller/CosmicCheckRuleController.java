package com.ck.cosmic.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.cosmic.service.CosmicCheckRuleService;
import com.ck.cosmic.vo.CosmicCheckRule;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Api("cosmic检查规则接口")
@RequestMapping("cosmic/check/rule")
public class CosmicCheckRuleController {

    @Autowired
    private CosmicCheckRuleService ruleServ;

    @Post("import")
    @ApiOperation("导入cosmic检查规则")
    public Object importRules(@RequestBody List<CosmicCheckRule> rules) {
        return ruleServ.saveRules(rules);
    }

    @Get("get")
    @ApiOperation("获取cosmic检查规则")
    public Object getRules() {
        return ruleServ.getRules();
    }

    @Post("save")
    @ApiOperation("保存cosmic检查规则")
    public Object saveRule(@RequestBody CosmicCheckRule rule) {
        return ruleServ.saveRule(rule);
    }

    @Post("delete")
    @ApiOperation("删除cosmic检查规则")
    public Object deleteRules(@RequestBody List<Long> ruleIds) {
        return ruleServ.deleteRules(ruleIds);
    }

    @Get("search")
    @ApiOperation("查询cosmic检查规则列表信息")
    public Object searchRules(@RequestParam(value = "keyWord", defaultValue = "") String keyWord,
                              @RequestParam("pageNum") int pageNum,
                              @RequestParam("pageSize") int pageSize) {
        return ruleServ.searchRules(keyWord, pageNum, pageSize);
    }
}
