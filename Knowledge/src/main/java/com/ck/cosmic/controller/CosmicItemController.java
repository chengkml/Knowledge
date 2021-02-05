package com.ck.cosmic.controller;

import com.ck.common.aop.Get;
import com.ck.cosmic.service.CosmicItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("Cosmic项目接口")
@RequestMapping("cosmic/item")
public class CosmicItemController {

    @Autowired
    private CosmicItemService itemServ;

    @Get("get/demand")
    @ApiOperation("查询cosmic需求信息")
    public Object getDemand(@RequestParam(name="id") Long demandId ) {
        return itemServ.getDemand(demandId);
    }
}
