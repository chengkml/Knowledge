package com.ck.job.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.job.po.JobPo;
import com.ck.job.service.JobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api("任务操作接口")
@RestController
@RequestMapping("job")
public class JobController {

    @Autowired
    private JobService jobServ;

    @Post("save")
    @ApiOperation("保存任务")
    public Object add(@RequestBody JobPo po) throws SchedulerException {
        jobServ.saveJob(po);
        return po;
    }

    @Post("delete/{id}")
    @ApiOperation("删除任务")
    public void delete(@PathVariable Long id) throws SchedulerException {
        jobServ.deleteJob(id);
    }

    @Get("search")
    @ApiOperation("查询任务")
    public Object search(@RequestParam("pageNum") int pageNum,
                         @RequestParam("pageSize") int pageSize,
                         @RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
        return jobServ.search(pageNum, pageSize, keyWord);
    }

    @Post("sync/all")
    @ApiOperation("同步所有任务")
    public Object syncAll() throws SchedulerException {
        return jobServ.syncAll();
    }
}
