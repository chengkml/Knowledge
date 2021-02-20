package com.ck.bat.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.bat.po.BatPo;
import com.ck.bat.service.BatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.vfs2.FileSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URISyntaxException;

@Api("bat接口")
@RestController
@RequestMapping("bat")
public class BatController {

    @Autowired
    private BatService batServ;

    @ApiOperation("启动bat")
    @Post("exe")
    public Object start(@RequestBody Long batId) throws IOException {
        return batServ.start(batId);
    }

    @ApiOperation("保存bat")
    @Post("save")
    public Object save(@RequestBody BatPo batPo) {
        return batServ.saveBat(batPo);
    }

    @ApiOperation("删除bat")
    @Post("delete")
    public Object delete(@RequestBody Long batId) throws FileSystemException, URISyntaxException {
        return batServ.deleteBat(batId);
    }

    @ApiOperation("查询bat")
    @Get("list")
    public Object list(@RequestParam(name = "keyWord", defaultValue = "") String keyWord,
                       @RequestParam("pageNum") int pageNum,
                       @RequestParam("pageSize") int pageSize) {
        return batServ.list(keyWord, pageNum, pageSize);
    }

    @ApiOperation("查询运行进程")
    @Get("list/process")
    public Object listProcess() {
        return batServ.listProcess();
    }

    @ApiOperation("停止运行的进程")
    @Post("stop/process")
    public Object stopProcess(@RequestBody String processId) {
        return batServ.stopProcess(processId);
    }
}
