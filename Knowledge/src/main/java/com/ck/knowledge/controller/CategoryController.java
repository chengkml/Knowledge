package com.ck.knowledge.controller;

import com.ck.knowledge.dao.CategoryRepository;
import com.ck.knowledge.po.CategoryPo;
import com.ck.knowledge.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Title: CategoryController
 * @Description: 知识点类目服务控制器
 * @Author: Chengkai
 * @Date: 2019/6/19 21:15
 * @Version: 1.0
 */
@RestController
@RequestMapping("category/")
public class CategoryController {

    @Autowired
    private CategoryRepository repo;

    @Autowired
    private CategoryService serv;

    @PostMapping("save")
    public Long add(@RequestBody CategoryPo cg){
        repo.save(cg);
        return cg.getId();
    }

    @PostMapping("delete/{id}")
    public void delete(@PathVariable long id){
        repo.deleteById(id);
    }

    @GetMapping("search")
    public List<CategoryPo> search(){
        return repo.findAll();
    }

    @GetMapping("getTree")
    public List<CategoryPo> getTree(){
        return serv.getTree();
    }
}
