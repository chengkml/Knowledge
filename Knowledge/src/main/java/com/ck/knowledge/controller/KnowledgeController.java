package com.ck.knowledge.controller;

import com.ck.knowledge.aop.Get;
import com.ck.knowledge.aop.Post;
import com.ck.knowledge.po.KnowledgePo;
import com.ck.knowledge.service.KnowledgeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: KnowledgeController
 * @Description: 知识点录入和查询服务控制器
 * @Author: Chengkai
 * @Date: 2019/6/19 19:46
 * @Version: 1.0
 */
@Api("知识点操作接口")
@RestController
@RequestMapping("knowledge")
public class KnowledgeController {

    @Autowired
    private KnowledgeService serv;

    @Post("save")
    @ApiOperation("保存知识点")
    public Object add(@RequestBody KnowledgePo po) {
        serv.saveKnowledge(po);
        return po;
    }

    @Post("delete/{id}")
    @ApiOperation("删除知识点")
    public void delete(@PathVariable Long id) {
        serv.deleteKnowledge(id);
    }

    @Get("search")
    @ApiOperation("查询知识点")
    public Object search(@RequestParam("pageNum") int pageNum,
                         @RequestParam("pageSize") int pageSize,
                         @RequestParam(value = "category", defaultValue = "-1") long category,
                         @RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
        return serv.search(pageNum, pageSize, keyWord, category);
    }

    @Get("getKnowledgeTree")
    @ApiOperation("查询知识架构树")
    public Object getKnowledgeTree(@RequestParam(name = "nodeId", defaultValue = "-1") long nodeId) {
        if (nodeId < 0) {
            return serv.getKnowledgeTree();
        } else {
            return serv.getKnowledgeTree(nodeId);
        }
    }
}
