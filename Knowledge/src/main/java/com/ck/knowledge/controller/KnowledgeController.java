package com.ck.knowledge.controller;

import com.ck.knowledge.dao.KnowledgeRepository;
import com.ck.knowledge.po.CategoryPo;
import com.ck.knowledge.po.KnowledgePo;
import com.ck.knowledge.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: KnowledgeController
 * @Description: 知识点录入和查询服务控制器
 * @Author: Chengkai
 * @Date: 2019/6/19 19:46
 * @Version: 1.0
 */
@RestController
@RequestMapping("knowledge")
public class KnowledgeController {
    @Autowired
    private KnowledgeRepository repo;

    @Autowired
    private KnowledgeService serv;

    @PostMapping("save")
    public KnowledgePo add(@RequestBody KnowledgePo po){
        repo.save(po);
        return po;
    }

    @PostMapping("delete/{id}")
    public void delete(@PathVariable Long id){
        repo.deleteById(id);
    }

    @GetMapping("search")
    public Page<KnowledgePo> search(@RequestParam("pageNum") int pageNum,
                                  @RequestParam("pageSize") int pageSize,
                                    @RequestParam(value = "category",defaultValue = "-1") long category,
                                  @RequestParam(value = "keyWord",defaultValue = "") String keyWord){
        return serv.search(pageNum,pageSize,keyWord,category);
    }

    @GetMapping("getKnowledgeTree")
    public List<CategoryPo> getKnowledgeTree(@RequestParam(name="nodeId",defaultValue = "-1") long nodeId){
        if(nodeId<0){
            return serv.getKnowledgeTree();
        }else{
            return serv.getKnowledgeTree(nodeId);
        }
    }
}
