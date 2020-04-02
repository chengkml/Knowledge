package com.ck.journal.controller;

import com.ck.journal.dao.CategoryRepository;
import com.ck.journal.po.CategoryPo;
import com.ck.journal.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Title: CategoryController
 * @Author: Chengkai
 * @Date: 2019/8/20 13:13
 * @Version: 1.0
 */
@RestController
@RequestMapping("category/")
public class CategoryController {

    @Autowired
    private CategoryRepository cateRepo;

    @Autowired
    private CategoryService cateServ;

    @GetMapping("searchDtRange")
    public List<CategoryPo> searchDtRange(@RequestParam("startTime") long startTime,@RequestParam("endTime") long endTime) {
        return cateRepo.searchDtRange(new Date(startTime),new Date(endTime));
    }

    @GetMapping("searchByType")
    public List<CategoryPo> searchByType(@RequestParam("type") String type) {
        return cateRepo.searchByType(type);
    }

    @GetMapping("cateTree")
    public List<CategoryPo> getCateTree(@RequestParam("startTime") long startTime,@RequestParam("endTime") long endTime) {
        return cateServ.getCateTree(new Date(startTime),new Date(endTime));
    }

    @GetMapping("test")
    public List<CategoryPo> test() {
        return null;
    }
}
