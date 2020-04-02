package com.ck.journal.controller;

import com.ck.journal.po.JournalPo;
import com.ck.journal.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

/**
 * @Title: JournalController
 * @Author: Chengkai
 * @Date: 2019/8/20 13:12
 * @Version: 1.0
 */
@RestController
@RequestMapping("journal/")
public class JournalController {

    @Autowired
    private JournalService journalServ;

    @PostMapping("save")
    public JournalPo add(@RequestBody JournalPo po){
        journalServ.save(po);
        return po;
    }

    @GetMapping("search")
    public Page<JournalPo> search(@RequestParam(value="keyWord",defaultValue = "") String keyWord,
                                  @RequestParam(value="pageSize") int pageSize,
                                  @RequestParam(value="pageNum") int pageNum,
                                  @RequestParam(value="startTime",defaultValue = "") Long startTime,
                                  @RequestParam(value="endTime",defaultValue = "") Long endTime,
                                  @RequestParam(value="category") String category){
        return journalServ.search(keyWord,pageSize,pageNum,startTime,endTime,category);
    }

}
