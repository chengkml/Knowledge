package com.ck.knowledge.controller;

import com.ck.knowledge.aop.Get;
import com.ck.knowledge.aop.Post;
import com.ck.knowledge.po.bat.BatPo;
import com.ck.knowledge.service.BatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api("bat接口")
@RestController
@RequestMapping("bat")
public class BatController {

    @Autowired
    private BatService batServ;

    @ApiOperation("执行bat")
    @Get("exe")
    public Object generateApi() throws IOException {
        batServ.exe();
        return "";
    }

    @ApiOperation("保存bat")
    @Post("save")
    public Object save(@RequestBody BatPo batPo) {
        return batServ.saveBat(batPo);
    }

    @ApiOperation("删除bat")
    @Post("delete")
    public Object delete(@RequestBody Long batId) {
        return batServ.deleteBat(batId);
    }

    @ApiOperation("查询bat")
    @Get("list")
    public Object list(@RequestParam(name = "keyWord", defaultValue = "") String keyWord,
                       @RequestParam("pageNum") int pageNum,
                       @RequestParam("pageSize") int pageSize) {
        return batServ.list(keyWord, pageNum, pageSize);
    }
}
